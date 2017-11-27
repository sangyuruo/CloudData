package com.distribution.data.repository;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.domain.SmartMeterData;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the SmartMeterData entity.
 */
@Repository
public class SmartMeterDataRepository {

    @Inject
    private Session session;
    @Inject
    private ModbusServerRepository modbusServerRepository;

    @Inject
    private SmartMeterRepository smartMeterRepository;

    private Mapper<SmartMeterData> mapper;

    private PreparedStatement findAllStmt;

//    protected PreparedStatement insertByNameStmt;

//    private PreparedStatement deleteByNameStmt;

    private PreparedStatement truncateStmt;
//    private PreparedStatement truncateByNameStmt;

    @PostConstruct
    public void init() {//smartMeterData_by_meter
        mapper = new MappingManager(session).mapper(SmartMeterData.class);
        findAllStmt = session.prepare("SELECT * FROM smartMeterData");
//        insertByNameStmt = session.prepare(
//                "INSERT INTO smartMeterData_by_meter (meterId, id) " +
//                    "VALUES (:meterId, :id)");
//        deleteByNameStmt = session.prepare(
//                "DELETE FROM smartMeterData_by_meter " +
//                    "WHERE meterId = :meterId");
//
        truncateStmt = session.prepare("TRUNCATE smartMeterData");
//        truncateByNameStmt = session.prepare("TRUNCATE smartMeterData_by_meter");
    }

    public List<SmartMeterData> findAll() {
        List<SmartMeterData> smartMeterData = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
            	 SmartMeterData smd = new SmartMeterData();
                 smd.setId(row.getUUID("id"));
                 smd.setMeterId(row.getUUID("meterId"));
                 smd.setHostname(row.getString("hostname"));
                 smd.setCompanyId(row.getUUID("companyId"));
                 smd.setServerId(row.getUUID("serverId"));
                 smd.setName(row.getString("name"));
                 smd.setServerCode(row.getString("serverCode"));
                 smd.setYmd(row.getInt("ymd"));
                 smd.setHour(row.getInt("hour"));
                 smd.setMinute(row.getInt("minute"));
                 smd.setSec(row.getInt("sec"));
                 smd.setIp(row.getString("ip"));
                 smd.setCode(row.getInt("code"));
                 smd.setCategory(row.getInt("category"));
                 smd.setData(row.getMap("data", String.class, Float.class));
                 smd.setAuxiliary(row.getMap("auxiliary", String.class, String.class));
                 return smd;
            }
        ).forEach(smartMeterData::add);
        return smartMeterData;
    }

    public int batchSave(List<SmartMeterData> list) {
    	BatchStatement batch = new BatchStatement();
//    	SmartMeter sm = null;
//    	ModbusSlave ms = null;
    	for (SmartMeterData smartMeterData : list) {
//            if(sm == null || ms ==null) {
//                sm = smartMeterRepository.findOneById(smartMeterData.getMeterId()).get();
//                ms = modbusServerRepository.findOne(sm.getServerId(), sm.getCompanyId());
//            }
//            smartMeterData.setCompanyId(ms.getCompanyId());
//            smartMeterData.setServerId(ms.getId());
//            smartMeterData.setServerCode(ms.getCode());
//            smartMeterData.setHostname(ms.getHostname());
    		batch.add(mapper.saveQuery(smartMeterData));
		}
        session.execute(batch);
        return list.size();
    }

    public SmartMeterData findOne(UUID id, UUID meterId, UUID companyId) {
        return mapper.get(id, meterId, companyId);
    }

    public SmartMeterData save(SmartMeterData smartMeterData) {
        if (smartMeterData.getId() == null) {
            smartMeterData.setId(UUIDs.timeBased());
        }
//        SmartMeter sm = smartMeterRepository.findOneById(smartMeterData.getMeterId()).get();
//        ModbusSlave  ms = modbusServerRepository.findOne(sm.getServerId(), sm.getCompanyId());
//        smartMeterData.setCompanyId(ms.getCompanyId());
//        smartMeterData.setServerId(ms.getId());
//        smartMeterData.setServerCode(ms.getCode());
//        smartMeterData.setHostname(ms.getHostname());
        mapper.save(smartMeterData);
        return smartMeterData;
    }

    public void delete(UUID id, UUID meterId, UUID companyId) {
        BatchStatement batch = new BatchStatement();
        SmartMeterData com = findOne(id, meterId, companyId);
        batch.add(mapper.deleteQuery(com));
//        batch.add(deleteByNameStmt.bind().setUUID("meterId", com.getMeterId()));
        session.execute(batch);
    }

    public void deleteAll() {
    	BatchStatement batch = new BatchStatement();
  		batch.add(truncateStmt.bind());
//  		batch.add(truncateByNameStmt.bind());
  		session.execute(batch);
    }
}
