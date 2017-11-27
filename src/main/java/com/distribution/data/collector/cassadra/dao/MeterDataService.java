package com.distribution.data.collector.cassadra.dao;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.type.DateUtils;
import com.distribution.data.domain.SmartMeterData;
import com.distribution.data.repository.SmartMeterDataRepository;
import com.distribution.modules.ws.MeterData;
import com.distribution.modules.ws.StringFloatMap;
import com.distribution.modules.ws.StringStringMap;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MeterDataService extends SmartMeterDataRepository {
	@Inject
    private Session session;
    private Mapper<SmartMeterData> mapper;
    private PreparedStatement findByIdStmt;
    private PreparedStatement findByIdAndCompanyIdStmt;
    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(SmartMeterData.class);
        findByIdStmt = session.prepare("SELECT * FROM SmartMeterData where id>=:startId and id<=:endId allow filtering");
        findByIdAndCompanyIdStmt = session.prepare("SELECT * FROM SmartMeterData where id>=:startId and id<=:endId and companyId=:companyId allow filtering");
    }

    public List<com.distribution.modules.ws.MeterData> findMeterDataByIdAndCompanyIdForService(LocalDateTime dt, UUID companyId) {
        if(dt.getSecond() >= 30){
            dt = dt.minusSeconds(dt.getSecond());
        }else{
            dt = dt.minusMinutes(1);
            dt = dt.minusSeconds(dt.getSecond() + 30);
        }
        dt = dt.minusNanos(dt.getNano());
        int sec = dt.getSecond();
        String ymd = DateUtils.formatDate(dt, "yyyyMMdd");
        int hour = dt.getHour();
        int minute = dt.getMinute();
        UUID startId = UUIDs.startOf(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        LocalDateTime ed = DateUtils.parseDate(ymd).atTime(hour, minute, sec);
        UUID endId = UUIDs.endOf(ed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<com.distribution.modules.ws.MeterData> smartMeters = new ArrayList<>();
        BoundStatement stmt = null;
        if(companyId == null){
            stmt = findByIdStmt.bind();
        }else{
            stmt = findByIdAndCompanyIdStmt.bind();
        }
        stmt.setUUID("startId", startId);
        stmt.setUUID("endId", endId);
        if(companyId != null) {
            stmt.setUUID("companyId", companyId);
        }
        session.execute(stmt).all().stream().map(
            row -> {
                return getMeterData(ymd, hour, minute, sec, row);
            }
        ).forEach(smartMeters::add);
        return smartMeters;
    }

    public List<com.distribution.modules.ws.MeterData> findMeterDataByIdForService(String ymd, int hour, int minute, int sec) {

    	LocalDateTime dt = DateUtils.parseDate(ymd).atTime(hour, minute, sec);
		UUID startId = UUIDs.startOf(dt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		LocalDateTime ed = DateUtils.parseDate(ymd).atTime(hour, minute, sec + 29);
		UUID endId = UUIDs.endOf(ed.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<com.distribution.modules.ws.MeterData> smartMeters = new ArrayList<>();
        BoundStatement stmt =  findByIdStmt.bind();
        stmt.setUUID("startId", startId);
        stmt.setUUID("endId", endId);
        session.execute(stmt).all().stream().map(
            row -> {
                return getMeterData(ymd, hour, minute, sec, row);
            }
        ).forEach(smartMeters::add);
        return smartMeters;
    }

    protected MeterData getMeterData(String ymd, int hour, int minute, int sec, Row row) {
        MeterData smd = new MeterData();
        smd.setId(row.getUUID("id").toString());
        smd.setYmd(ymd);
        smd.setHour(hour);
        smd.setMinute(minute);
        smd.setSec(sec);
        smd.setHostname(row.getString("hostname"));
        smd.setName(row.getString("name"));
        smd.setServerId(row.getUUID("serverId").toString());
        smd.setServerCode(row.getString("serverCode"));
        smd.setCompanyId(row.getUUID("companyId").toString());
        smd.setIp(row.getString("ip"));
        smd.setCode(row.getInt("code"));
        smd.setCategory(row.getInt("category"));
        StringFloatMap<String, Float> map = new StringFloatMap<String, Float>();
        map.putAll(row.getMap("data", String.class, Float.class));
        smd.setDataMap(map);
        StringStringMap aux = new StringStringMap();
        aux.putAll(row.getMap("auxiliary", String.class, String.class));
        smd.setAuxiliaryMap(aux);
        return smd;
    }

}
