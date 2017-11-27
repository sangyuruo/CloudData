package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.domain.ServerStatus;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Cassandra repository for the ServerStatus entity.
 */
@Repository
public class ServerStatusRepository {

    @Inject
    private Session session;

    private Mapper<ServerStatus> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement findOneByServerIdStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(ServerStatus.class);
        findAllStmt = session.prepare("SELECT * FROM serverStatus");
        findOneByServerIdStmt = session.prepare("SELECT * FROM serverStatus where serverId=:serverId and id>=:st and id<=:ed");
        truncateStmt = session.prepare("TRUNCATE serverStatus");
    }

    public List<ServerStatus> findAll() {
        List<ServerStatus> serverStatuses = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                return getServerStatus(row);
            }
        ).forEach(serverStatuses::add);
        return serverStatuses;
    }

    private ServerStatus getServerStatus(Row row) {
        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setId(row.getUUID("id"));
        serverStatus.serverId(row.getUUID("serverId"));

        Date lu = row.getTimestamp("lastUpdate");
        if(lu != null) {
            serverStatus.setLastUpdate(lu.toInstant().atZone(ZoneId.systemDefault()));
        }

        Date cd = row.getTimestamp("createDate");
        if(cd != null) {
            serverStatus.setCreateDate(cd.toInstant().atZone(ZoneId.systemDefault()));
        }

        serverStatus.setLastState(row.getInt("lastState"));
        serverStatus.setSuccess(row.getInt("success"));
        serverStatus.setTotal(row.getInt("total"));
        return serverStatus;
    }

    public ServerStatus findOne(UUID id, UUID serverId) {
        return mapper.get(id, serverId);
    }

    public Optional<ServerStatus> findOneByName(UUID serverId, LocalDateTime start, LocalDateTime end) {
    	BoundStatement stmt = findOneByServerIdStmt.bind();
        stmt.setUUID("serverId", serverId);
        UUID st = UUIDs.startOf(DateUtils.getTodayStart(start).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        UUID ed = UUIDs.endOf(DateUtils.getTodayEnd(end).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        stmt.setUUID("st", st);
        stmt.setUUID("ed", ed);
        return findOneFromIndex(stmt);
    }

    private Optional<ServerStatus> findOneFromIndex(BoundStatement stmt) {
        ResultSet rs = session.execute(stmt);
        if (rs.isExhausted()) {
            return Optional.empty();
        }
        return Optional.ofNullable(getServerStatus(rs.one()));
    }
    public ServerStatus save(ServerStatus status) {
        if(status.getId() == null){
            status.setId(UUIDs.timeBased());
        }
        mapper.save(status);
        return status;
    }

    public void delete(UUID id, UUID serverId) {
//        mapper.delete(id);
        BatchStatement batch = new BatchStatement();
        ServerStatus com = findOne(id, serverId);
        batch.add(mapper.deleteQuery(com));
        session.execute(batch);
    }

    public void deleteAll() {
    	BatchStatement batch = new BatchStatement();
		batch.add(truncateStmt.bind());
		session.execute(batch);
    }
}
