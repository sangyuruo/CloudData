package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.domain.ModbusServer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Cassandra repository for the ModbusSlave entity.
 */
@Repository
public class ModbusServerRepository {

    @Inject
    private Session session;

    private Mapper<ModbusServer> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement findOneByIdStmt;
    private PreparedStatement findOneByNameStmt;
    private PreparedStatement insertByNameStmt;

    private PreparedStatement deleteByNameStmt;
    private PreparedStatement truncateStmt;
    private PreparedStatement truncateByNameStmt;
    private PreparedStatement insertByCodeStmt;
    private PreparedStatement deleteByCodeStmt;
    private PreparedStatement truncateByCodeStmt;
    private PreparedStatement findOneByCodeStmt;
    private PreparedStatement deleteByIdAndCompanyIdStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(ModbusServer.class);
        findAllStmt = session.prepare("SELECT * FROM modbusServer");
        findOneByIdStmt = session.prepare("SELECT * FROM modbusServer where id=:id");
        findOneByNameStmt = session.prepare("SELECT id, companyId FROM modbusServer_by_hostname where hostname=:hostname");
        findOneByCodeStmt = session.prepare("SELECT id, companyId FROM modbusServer_by_code where code=:code");
        deleteByIdAndCompanyIdStmt = session.prepare("DELETE FROM modbusServer " +
                      "WHERE id=:id and companyId=:companyId");
        insertByNameStmt = session.prepare(
                "INSERT INTO modbusServer_by_hostname (hostname, companyId, id) " +
                    "VALUES (:hostname, :companyId, :id)");
        insertByCodeStmt = session.prepare(
                "INSERT INTO modbusServer_by_code (code, companyId, id) " +
                    "VALUES (:code, :companyId, :id)");
        deleteByNameStmt = session.prepare(
                "DELETE FROM modbusServer_by_hostname " +
                    "WHERE hostname = :hostname");
        truncateStmt = session.prepare("TRUNCATE modbusServer");
        deleteByCodeStmt = session.prepare(
            "DELETE FROM modbusServer_by_code " +
                "WHERE code = :code");
        truncateByCodeStmt = session.prepare("TRUNCATE modbusServer_by_code");
        truncateByNameStmt = session.prepare("TRUNCATE modbusServer_by_hostname");
    }

    public Optional<ModbusServer> findOneById(UUID id) {
        BoundStatement stmt = findOneByIdStmt.bind();
        stmt.setUUID("id", id);
        return findOneFromIndex(stmt);
    }

    private Optional<ModbusServer> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        Row row = rs.one();
        Optional<UUID> id = Optional.ofNullable(row.getUUID("id"));
        Optional<UUID> companyId = Optional.ofNullable(row.getUUID("companyId"));
        return Optional.ofNullable(mapper.get(id.get(), companyId.get()));
    }

    public List<ModbusServer> findAll() {
        List<ModbusServer> modbusServers = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                return getModbusServer(row);
            }
        ).forEach(modbusServers::add);
        return modbusServers;
    }

    protected ModbusServer getModbusServer(Row row) {
        ModbusServer modbusServer = new ModbusServer();
        modbusServer.setId(row.getUUID("id"));
        modbusServer.setHostname(row.getString("hostname"));
        modbusServer.setCompanyId(row.getUUID("companyId"));
        modbusServer.setCode(row.getString("code"));
        modbusServer.setIp(row.getString("ip"));
        modbusServer.setPort(row.getInt("port"));
        modbusServer.setEnable(row.getBool("enable"));
        modbusServer.setModel(row.getInt("model"));
        modbusServer.setEncapsulated(row.getBool("encapsulated"));
        modbusServer.setKeepAlive(row.getBool("keepAlive"));
        modbusServer.setRequestTimeout(row.getInt("requestTimeout"));
        modbusServer.setReplyTimeout(row.getInt("replyTimeout"));
        return modbusServer;
    }

    public ModbusServer findOne(UUID id, UUID companyId) {
        return mapper.get(id, companyId);
    }

    public Optional<ModbusServer> findOneByName(String hostname) {
    	BoundStatement stmt = findOneByNameStmt.bind();
        stmt.setString("hostname", hostname);
        return findOneFromIndex(stmt);
    }

    public Optional<ModbusServer> findOneByCode(String code) {
        BoundStatement stmt = findOneByCodeStmt.bind();
        stmt.setString("code", code);
        return findOneFromIndex(stmt);
    }

    public ModbusServer save(ModbusServer modbusServer) {
        if (modbusServer.getId() == null) {
            modbusServer.setId(UUID.randomUUID());
        }else{
//        	ModbusServer oldSer = mapper.get(modbusServer.getId(), modbusServer.getCompanyId());
            Optional<ModbusServer> oldSer = findOneById(modbusServer.getId());
        	if(oldSer.isPresent()){
        		session.execute(deleteByNameStmt.bind().setString("hostname", oldSer.get().getHostname()));
                session.execute(deleteByCodeStmt.bind().setString("code", oldSer.get().getCode()));
                //delete(oldSer.get().getId(), oldSer.get().getCompanyId());
                session.execute(deleteByIdAndCompanyIdStmt.bind().setUUID("id", oldSer.get().getId()).setUUID("companyId", oldSer.get().getCompanyId()));
        	}
        }
        BatchStatement batch = new BatchStatement();
        batch.add(mapper.saveQuery(modbusServer));
        batch.add(insertByCodeStmt.bind()
                .setString("code", modbusServer.getCode()).setUUID("companyId", modbusServer.getCompanyId())
                .setUUID("id", modbusServer.getId()));
        batch.add(insertByNameStmt.bind()
                .setString("hostname", modbusServer.getHostname()).setUUID("companyId", modbusServer.getCompanyId())
                .setUUID("id", modbusServer.getId()));
        session.execute(batch);
        return modbusServer;
    }

    public void delete(UUID id, UUID companyId) {
    	BatchStatement batch = new BatchStatement();
    	ModbusServer com = findOne(id, companyId);
        batch.add(mapper.deleteQuery(com));
        batch.add(deleteByNameStmt.bind().setString("hostname", com.getHostname()));
        batch.add(deleteByCodeStmt.bind().setString("code", com.getCode()));
        session.execute(batch);
    }

    public void deleteAll() {
		BatchStatement batch = new BatchStatement();
		batch.add(truncateStmt.bind());
		batch.add(truncateByNameStmt.bind());
        batch.add(truncateByCodeStmt.bind());
		session.execute(batch);
    }
}
