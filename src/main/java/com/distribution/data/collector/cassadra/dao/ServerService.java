package com.distribution.data.collector.cassadra.dao;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.repository.ModbusServerRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServerService extends ModbusServerRepository {

    @Inject
    private Session session;
    private Mapper<Server> mapper;
    private PreparedStatement findAllServerStmt;
    private PreparedStatement findModbusServerByCompanyIdStmt;
    private PreparedStatement findOneByNameStmt;


    @PostConstruct
    public void init() {
    	mapper = new MappingManager(session).mapper(Server.class);
    	findAllServerStmt = session.prepare("SELECT * FROM modbusServer");
    	findModbusServerByCompanyIdStmt = session.prepare("SELECT * FROM modbusServer where companyId = :companyId allow filtering ");
    	findOneByNameStmt = session.prepare("SELECT id, companyId FROM modbusServer_by_hostname where hostname = :hostname");
    }

    private Optional<Server> findOneServerFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        Row row = rs.one();
        Optional<UUID> id = Optional.ofNullable(row.getUUID("id"));
        Optional<UUID> companyId = Optional.ofNullable(row.getUUID("companyId"));
        return Optional.ofNullable(mapper.get(id.get(), companyId.get()));
    }

    public Optional<Server> findOneServerByName(String hostname) {
        BoundStatement stmt = findOneByNameStmt.bind();
        stmt.setString("hostname", hostname);
        return findOneServerFromIndex(stmt);
    }

    public Server findOneServer(UUID id, UUID companyId) {
        return mapper.get(id, companyId);
    }

	public List<Server> findModbusServerByCompanyId(UUID companyId){
		List<Server> modbusServers = new ArrayList<>();
        BoundStatement stmt =  findModbusServerByCompanyIdStmt.bind();
        stmt.setUUID("companyId", companyId);
        session.execute(stmt).all().stream().map(
            row -> {
                return getServer(row);
            }
        ).forEach(modbusServers::add);
        return modbusServers;
	}

	public List<Server> findAllServer() {
        List<Server> modbusServers = new ArrayList<>();
        BoundStatement stmt =  findAllServerStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                return getServer(row);
            }
        ).forEach(modbusServers::add);
        return modbusServers;
    }

    protected Server getServer(Row row) {
        Server modbusServer = new Server();
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
}
