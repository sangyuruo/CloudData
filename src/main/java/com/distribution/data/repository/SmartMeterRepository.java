package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.domain.SmartMeter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Cassandra repository for the SmartMeter entity.
 */
@Repository
public class SmartMeterRepository {

    @Inject
    private Session session;

    @Inject
    private ModbusServerRepository modbusServerRepository;

    private Mapper<SmartMeter> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement findByServerIdStmt;

    private PreparedStatement insertByServerStmt;

    private PreparedStatement deleteByServerStmt;

    private PreparedStatement truncateStmt;
    private PreparedStatement truncateByServerStmt;
    private PreparedStatement truncateByCompanyStmt;
    private PreparedStatement insertByCompanyStmt;
    private PreparedStatement deleteByCompanyStmt;
    private PreparedStatement findOneByIdStmt;
    private PreparedStatement findOneByServerAndCodeStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(SmartMeter.class);
        findAllStmt = session.prepare("SELECT * FROM smartMeter");
        findOneByIdStmt = session.prepare("SELECT * FROM smartMeter where id=:id");
        findByServerIdStmt = session.prepare("SELECT * FROM smartMeter where serverId=:serverId allow filtering");
        findOneByServerAndCodeStmt = session.prepare("SELECT id FROM smartMeter_by_server_and_code where serverId=:serverId and code=:code");
        insertByServerStmt = session.prepare(
                "INSERT INTO smartMeter_by_server_and_code (serverId, code, id) " +
                    "VALUES (:serverId, :code, :id)");
        deleteByServerStmt = session.prepare(
                "DELETE FROM smartMeter_by_server_and_code " +
                    "WHERE serverId = :serverId and code=:code");
        insertByCompanyStmt = session.prepare(
                "INSERT INTO smartMeter_by_company_and_server (companyId, serverId, id) " +
                    "VALUES (:companyId, :serverId, :id)");
        deleteByCompanyStmt = session.prepare(
                "DELETE FROM smartMeter_by_company_and_server " +
                    "WHERE companyId = :companyId and serverId = :serverId");
        truncateByCompanyStmt = session.prepare("TRUNCATE smartMeter_by_company_and_server");
        truncateStmt = session.prepare("TRUNCATE smartMeter");
        truncateByServerStmt = session.prepare("TRUNCATE smartMeter_by_server_and_code");
    }

    public List<SmartMeter> findByServerId(UUID serverId) {
        List<SmartMeter> smartMeters = new ArrayList<>();
        BoundStatement stmt =  findByServerIdStmt.bind();
        stmt.setUUID("serverId", serverId);
        session.execute(stmt).all().stream().map(
            row -> {
               return getSmartMeter(row);
            }
        ).forEach(smartMeters::add);
        return smartMeters;
    }

    public List<com.distribution.modules.ws.SmartMeter> findByServerIdForService(UUID serverId) {
        List<com.distribution.modules.ws.SmartMeter> smartMeters = new ArrayList<>();
        BoundStatement stmt =  findByServerIdStmt.bind();
        stmt.setUUID("serverId", serverId);
        session.execute(stmt).all().stream().map(
            row -> {
                return getSmartMeterForService(row);
            }
        ).forEach(smartMeters::add);
        return smartMeters;
    }
    private com.distribution.modules.ws.SmartMeter getSmartMeterForService(Row row) {
        com.distribution.modules.ws.SmartMeter smartMeter = new com.distribution.modules.ws.SmartMeter();
        smartMeter.setId(row.getUUID("id").toString());
        smartMeter.setServerId(row.getUUID("serverId").toString());
        smartMeter.setCompanyId(row.getUUID("companyId").toString());
        smartMeter.setCode("" + row.getInt("code"));
        smartMeter.setName(row.getString("name"));
        smartMeter.setCategory(row.getInt("category"));
        smartMeter.setLongitude(row.getDouble("longitude"));
        smartMeter.setLatitude(row.getDouble("latitude"));
        smartMeter.setEnabled(row.getBool("enable"));
//        smartMeter.setFunction(row.getInt("function"));
       // smartMeter.setBigEndian(row.getBool("bigEndian"));
       // smartMeter.setStartOffset(row.getInt("startOffset"));
       // smartMeter.setNumberOfRegisters(row.getInt("numberOfRegisters"));
        //smartMeter.setDataTypes(row.getMap("dataTypes", String.class, String.class));
        smartMeter.setCtlAddr(row.getInt("controlAddress"));
        return smartMeter;
    }

    public List<com.distribution.modules.ws.SmartMeter> findAllForService() {
        List<com.distribution.modules.ws.SmartMeter> smartMeters = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                return getSmartMeterForService(row);
            }
        ).forEach(smartMeters::add);
        return smartMeters;
    }

    private SmartMeter getSmartMeter(Row row) {
        SmartMeter smartMeter = new SmartMeter();
        smartMeter.setId(row.getUUID("id"));
        smartMeter.setServerId(row.getUUID("serverId"));
        smartMeter.setCompanyId(row.getUUID("companyId"));
        smartMeter.setCode(row.getInt("code"));
        smartMeter.setLongcode(row.getLong("longcode"));
        smartMeter.setName(row.getString("name"));
        smartMeter.setCategory(row.getInt("category"));
        smartMeter.setLongitude(row.getDouble("longitude"));
        smartMeter.setLatitude(row.getDouble("latitude"));
        smartMeter.setEnable(row.getBool("enable"));
        smartMeter.setBigEndian(row.getBool("bigEndian"));
        smartMeter.setStartOffset(row.getInt("startOffset"));
        smartMeter.setNumberOfRegisters(row.getInt("numberOfRegisters"));
        smartMeter.setAllowDuplicate(row.getBool("allowDuplicate"));
        smartMeter.setDataTypes(row.getMap("dataTypes", String.class, String.class));
        smartMeter.setControlAddress(row.getInt("controlAddress"));
        int func = row.getInt("func");
        func = func == 4 ? 4 : 3;
        smartMeter.setFunc(func);
        return smartMeter;
    }

    public List<SmartMeter> findAll() {
    	List<SmartMeter> smartMeters = new ArrayList<>();
    	BoundStatement stmt =  findAllStmt.bind();
    	session.execute(stmt).all().stream().map(
    			row -> {
    				return getSmartMeter(row);
    			}
    			).forEach(smartMeters::add);
    	return smartMeters;
    }

    public SmartMeter findOne(Long id, String serverId, int code) {
        return mapper.get(id, serverId, code);
    }

    public SmartMeter save(SmartMeter smartMeter) {
        if (smartMeter.getId() == null) {
            smartMeter.setId(UUID.randomUUID());
        }else{
        	Optional<SmartMeter> oldSer = findOneById(smartMeter.getId());
        	if(oldSer.isPresent()){
        	    delete(oldSer.get().getId(), oldSer.get().getServerId(), oldSer.get().getCode());
        		//session.execute(deleteByServerStmt.bind().setUUID("serverId", oldSer.get().getServerId()).setInt("code", oldSer.get().getCode()));
                //session.execute(deleteByCompanyStmt.bind().setUUID("companyId", oldSer.get().getServerId()).setUUID("serverId", oldSer.get().getServerId()));
        	}
        }
        smartMeter.setCompanyId(modbusServerRepository.findOneById(smartMeter.getServerId()).get().getCompanyId());
        BatchStatement batch = new BatchStatement();
        batch.add(mapper.saveQuery(smartMeter));
        batch.add(insertByCompanyStmt.bind()
                .setUUID("companyId", smartMeter.getCompanyId()).setUUID("serverId", smartMeter.getServerId())
                .setUUID("id", smartMeter.getId()));
        batch.add(insertByServerStmt.bind()
                .setUUID("serverId", smartMeter.getServerId()).setInt("code", smartMeter.getCode())
                .setUUID("id", smartMeter.getId()));
        session.execute(batch);
        return smartMeter;
    }

    public void delete(Long id, String serverId, int code) {
//        mapper.delete(id, serverId);
        BatchStatement batch = new BatchStatement();
        SmartMeter com = findOne(id, serverId, code);
        batch.add(mapper.deleteQuery(com));
        batch.add(deleteByServerStmt.bind().setUUID("serverId", com.getServerId()).setInt("code", com.getCode()));
        batch.add(deleteByCompanyStmt.bind().setUUID("companyId", com.getCompanyId()).setUUID("serverId", com.getServerId()));
        session.execute(batch);
    }

    public void deleteAll() {
    	BatchStatement batch = new BatchStatement();
		batch.add(truncateStmt.bind());
		batch.add(truncateByCompanyStmt.bind());
        batch.add(truncateByServerStmt.bind());
		session.execute(batch);
    }

    public Optional<SmartMeter> findOneById(Long id) {
        BoundStatement stmt = findOneByIdStmt.bind();
        stmt.setUUID("id", id);
        return findOneFromIndex(stmt);
    }
    private Optional<SmartMeter> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        return  Optional.ofNullable(getSmartMeter(rs.one()));
    }

    public Optional<SmartMeter> findOneByCode(UUID serverId, Integer code) {
        BoundStatement stmt = findOneByServerAndCodeStmt.bind();
        stmt.setUUID("serverid", serverId);
        stmt.setInt("code", code);
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        Row row = rs.one();
        Optional<UUID> id = Optional.ofNullable(row.getUUID("id"));
        return Optional.ofNullable(mapper.get(id.get(), serverId, code));
    }
}
