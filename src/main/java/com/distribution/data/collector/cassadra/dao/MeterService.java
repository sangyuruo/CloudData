package com.distribution.data.collector.cassadra.dao;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.distribution.data.collector.cassadra.entity.Meter;
import com.distribution.data.repository.SmartMeterRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MeterService extends SmartMeterRepository {
	@Inject
    private Session session;
    private Mapper<Meter> mapper;
    private PreparedStatement findByServerIdStmt;


  /*  @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Meter.class);
        findByServerIdStmt = session.prepare("SELECT * FROM smartMeter where serverId=:serverId allow filtering");
    }

	 public List<Meter> findMetersByServerId(UUID serverId) {
	        List<Meter> smartMeters = new ArrayList<>();
	        BoundStatement stmt =  findByServerIdStmt.bind();
	        stmt.setUUID("serverId", serverId);
	        session.execute(stmt).all().stream().map(
	            row -> {
                    return getMeter(row);
                }
	        ).forEach(smartMeters::add);
	        return smartMeters;
	}
    public Meter findOneMeter(UUID id, UUID serverId, int code) {
        return mapper.get(id, serverId, code);
    }
    protected Meter getMeter(Row row) {
        Meter smartMeter = new Meter();
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
        smartMeter.setAllowDuplicate(row.getBool("allowDuplicate"));
        smartMeter.setNumberOfRegisters(row.getInt("numberOfRegisters"));
        smartMeter.setDataTypes(row.getMap("dataTypes", String.class, String.class));
        smartMeter.setControlAddress(row.getInt("controlAddress"));
        int func = row.getInt("func");
        func = func == 4 ? 4 : 3;
        smartMeter.setFunc(func);
        return smartMeter;
    }
*/
}
