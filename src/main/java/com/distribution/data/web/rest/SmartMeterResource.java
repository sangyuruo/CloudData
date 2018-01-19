package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cassadra.entity.Server;
import com.distribution.data.collector.cmd.MeterExecuter;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.repository.SmartMeterStatusRepository;

import com.distribution.data.service.ComPointService;
import com.distribution.data.service.MeterInfoService;
import com.distribution.data.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * REST controller for managing SmartMeter.
 */
@RestController
@RequestMapping("/api")
public class SmartMeterResource implements ApplicationEventPublisherAware {
    public static final String ADD = "SMARTMETER_ADD";
    public static final String UPDATE = "SMARTMETER_UPDATE";
    public static final String DEL = "SMARTMETER_DELETE";
    private final Logger log = LoggerFactory.getLogger(SmartMeterResource.class);
    private ApplicationEventPublisher eventPublisher;
    @Inject
    private MeterInfoService smartMeterRepository;
    @Inject
    private MeterExecuter meterExecuter;
    @Inject
    @Named("modbusServerRepository")
    private ComPointService modbusServerRepository;

    @Inject
    private SmartMeterStatusRepository smartMeterStatusRepository;

    /**
     * GET /smart-meters/:id : get the "id" smartMeter.
     *
     * @param id the id of the smartMeterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @GetMapping("/smart-meters/sw/{id}/{isActivated}")
    @Timed
    public ResponseEntity<Void> switchStatus(@PathVariable String id, @PathVariable String isActivated) {
        log.debug("REST request to Switch SmartMeter status : {}, {}", id, isActivated);

        SmartMeter meter = smartMeterRepository.findOneById(id);
        //smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));

        Server server = modbusServerRepository.findOneById( meter.getServerId());

        String cmd = Boolean.parseBoolean(isActivated) ? meter.getDataTypes().get("on") : meter.getDataTypes().get("off");
        String st = meter.getDataTypes().get("rst");
        if(cmd != null) {
        	try{
        		meterExecuter.commandExecue(server.getCode(), meter.getCode(), cmd);
        	}catch(Throwable ex){
        		throw ex;
        	}
            if(st != null){
                try {
                    Thread.currentThread().sleep(500);
                    meterExecuter.commandExecue(server.getCode(), meter.getCode(), st);
                } catch (InterruptedException e) {
                    log.error("命令执行异常。");
                }
            }
        }

        return ResponseEntity.ok().headers(HeaderUtil.createEntitySwitchAlert("smartMeter", meter.getName())).build();
    }

    /**
     * 下发单个开关命令
     * @param id
     * @param childIndex
     * @param isActivated
     * @return
     */
    @GetMapping("/smart-meters/sw/{id}/{childIndex}/{isActivated}")
    @Timed
    public ResponseEntity<Void> switchZY112Status(@PathVariable String id, @PathVariable Integer childIndex, @PathVariable String isActivated) {
        log.debug("REST request to Switch SmartMeter status : {}, {}", id, isActivated);
        SmartMeter meter = smartMeterRepository.findOneById(id); //smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));
        String cmd = null;
		String[] openCmd = { "050600030001B98E", "050600040001084F",
				"050600050001598F", "050600060001A98F", "050600070001F84F",
				"050600080001C84C", "050600090001998C", "0506000A0001698C",
				"0506000B0001384C", "0506000C0001898D", "0506000D0001D84D",
				"0506000E0001284D" };
		String[] closeCmd = { "050600030000784E", "050600040000C98F",
				"050600050000984F", "050600060000684F", "050600070000398F",
				"050600080000098C", "050600090000584C", "0506000A0000A84C",
				"0506000B0000F98C", "0506000C0000484D", "0506000D0001D84D",
				"0506000E0000E98D" };
        boolean flag = Boolean.parseBoolean(isActivated);

        if( flag ){
        	cmd = openCmd[childIndex-1];
        }else{
        	cmd = closeCmd[childIndex-1];
        }
        Server server = modbusServerRepository.findOneById(meter.getServerId());
        String st = meter.getDataTypes().get("rst");
        if(cmd != null) {
        	try{
        		meterExecuter.commandExecue(server.getCode(), meter.getCode(), cmd);
        	}catch(Throwable ex){
        		throw ex;
        	}
            if(st != null){
                try {
                    Thread.currentThread().sleep(500);
                    meterExecuter.commandExecue(server.getCode(), meter.getCode(), st);
                } catch (InterruptedException e) {
                    log.error("命令执行异常。");
                }
            }
        }

        return ResponseEntity.ok().headers(HeaderUtil.createEntitySwitchAlert("smartMeter", meter.getName())).build();
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher =  applicationEventPublisher;
	}

}
