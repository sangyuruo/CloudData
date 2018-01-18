package com.distribution.data.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.domain.SmartMeterStatus;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Cassandra repository for the SmartMeterStatus entity.
 */
@Repository
public class SmartMeterStatusRepository {

    @Inject
    private Session session;

    private Mapper<SmartMeterStatus> mapper;

    private PreparedStatement findAllStmt;
    private PreparedStatement findOneByMeterIdStmt;
    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(SmartMeterStatus.class);
        findAllStmt = session.prepare("SELECT * FROM smartMeterStatus");
        findOneByMeterIdStmt = session.prepare("SELECT * FROM smartMeterStatus where meterId=:meterId and id>=:st and id<=:ed");
        truncateStmt = session.prepare("TRUNCATE smartMeterStatus");
    }

    public List<SmartMeterStatus> findAll() {
        List<SmartMeterStatus> smartMeterStatuses = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                return getSmartMeterStatus(row);
            }
        ).forEach(smartMeterStatuses::add);
        return smartMeterStatuses;
    }

    private SmartMeterStatus getSmartMeterStatus(Row row) {
        SmartMeterStatus smartMeterStatus = new SmartMeterStatus();
        smartMeterStatus.setId(row.getUUID("id"));
        smartMeterStatus.setMeterId(row.getUUID("meterId"));
        Date lu = row.getTimestamp("lastUpdate");
        if(lu != null) {
            smartMeterStatus.setLastUpdate(lu.toInstant().atZone(ZoneId.systemDefault()));
        }
        Date cd = row.getTimestamp("createDate");
        if(cd != null) {
            smartMeterStatus.setCreateDate(cd.toInstant().atZone(ZoneId.systemDefault()));
        }
        smartMeterStatus.setLastState(row.getInt("lastState"));

        Date lrd = row.getTimestamp("lastRetryDate");
        if(lrd != null) {
            smartMeterStatus.setLastRetryDate(lrd.toInstant().atZone(ZoneId.systemDefault()));
        }
        smartMeterStatus.setLastRetryLevel(row.getInt("lastRetryLevel"));
        smartMeterStatus.setRetryCount(row.getInt("retryCount"));
        smartMeterStatus.setSuccess(row.getInt("success"));
        smartMeterStatus.setTotal(row.getInt("total"));
        smartMeterStatus.setVolume(row.getFloat("volume"));
        smartMeterStatus.setSwitchStatus(row.getInt("switchStatus"));
        return smartMeterStatus;
    }

    public SmartMeterStatus findOne(UUID id, UUID meterId) {
        return mapper.get(id, meterId);
    }

    public Optional<SmartMeterStatus> findOneByName(String meterId, LocalDateTime start, LocalDateTime end) {
            BoundStatement stmt = findOneByMeterIdStmt.bind();
            stmt.setString("meterId", meterId);
            UUID st = UUIDs.startOf(DateUtils.getTodayStart(start).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            UUID ed = UUIDs.endOf(DateUtils.getTodayEnd(end).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            stmt.setUUID("st", st);
            stmt.setUUID("ed", ed);
            return findOneFromIndex(stmt);
        }


        private Optional<SmartMeterStatus> findOneFromIndex(BoundStatement stmt) {
            ResultSet rs = session.execute(stmt);
            if (rs.isExhausted()) {
                return Optional.empty();
            }
            return Optional.ofNullable(getSmartMeterStatus(rs.one()));
        }

    public SmartMeterStatus save(SmartMeterStatus status) {
        if(status.getId() == null){
            status.setId(UUIDs.timeBased());
        }
        mapper.save(status);
        return status;
    }

    public int batchSave(List<SmartMeterStatus> list) {
        BatchStatement batch = new BatchStatement();
        for (SmartMeterStatus smartMeterStatus : list) {
            batch.add(mapper.saveQuery(smartMeterStatus));
        }
        session.execute(batch);
        return list.size();
    }

    public void delete(UUID id, UUID meterId) {
//      mapper.delete(id);
        BatchStatement batch = new BatchStatement();
        SmartMeterStatus com = findOne(id, meterId);
        batch.add(mapper.deleteQuery(com));
        session.execute(batch);
    }

    public void deleteAll() {
        BatchStatement batch = new BatchStatement();
        batch.add(truncateStmt.bind());
        session.execute(batch);
    }
}
