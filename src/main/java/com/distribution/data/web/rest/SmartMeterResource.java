package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.cmd.MeterExecuter;
import com.distribution.data.collector.event.ModbusReloadEvent;
import com.distribution.data.domain.ModbusServer;
import com.distribution.data.domain.SmartMeter;
import com.distribution.data.domain.SmartMeterStatus;
import com.distribution.data.repository.ModbusServerRepository;
import com.distribution.data.repository.SmartMeterRepository;
import com.distribution.data.repository.SmartMeterStatusRepository;
import com.distribution.data.service.dto.SmartMeterDTO;
import com.distribution.data.service.mapper.SmartMeterMapper;
import com.distribution.data.security.SecurityUtils;
import com.distribution.data.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
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
    private SmartMeterRepository smartMeterRepository;
    @Inject
    private MeterExecuter meterExecuter;
    @Inject
    @Named("modbusServerRepository")
    private ModbusServerRepository modbusServerRepository;

    @Inject
    private SmartMeterMapper smartMeterMapper;
    @Inject
    private SmartMeterStatusRepository smartMeterStatusRepository;

    /**
     * POST  /smart-meters : Create a new smartMeter.
     *
     * @param smartMeterDTO the smartMeterDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartMeterDTO, or with status 400 (Bad Request) if the smartMeter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smart-meters")
    @Timed
    public ResponseEntity<SmartMeterDTO> createSmartMeter(@Valid @RequestBody SmartMeterDTO smartMeterDTO) throws URISyntaxException {
        log.debug("REST request to save SmartMeter : {}", smartMeterDTO);
        if (smartMeterDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("smartMeter", "idexists", "A new smartMeter cannot already have an ID")).body(null);
        }
        Optional<SmartMeter> old = smartMeterRepository.findOneByCode(smartMeterDTO.getServerId(), smartMeterDTO.getCode());
        if(old.isPresent()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modbusServer", "codeexists", "A new smartMeter cannot already have a code")).body(null);
        }
        SmartMeter smartMeter = smartMeterMapper.smartMeterDTOToSmartMeter(smartMeterDTO);
        smartMeter = smartMeterRepository.save(smartMeter);
        ModbusReloadEvent reload = new ModbusReloadEvent(smartMeter);
        reload.setOperator(ModbusReloadEvent.OPERATOR_ADD);
        reload.setType(ModbusReloadEvent.MODBUS_METER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(smartMeter, ADD));
        SmartMeterDTO result = smartMeterMapper.smartMeterToSmartMeterDTO(smartMeter);
        return ResponseEntity.created(new URI("/api/smart-meters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("smartMeter", result.getName()))
            .body(result);
    }

    private AuditApplicationEvent createAudit(SmartMeter com, String type){
        Map<String, Object> map = new HashMap<>();
        map.put("id", com.getId());
        map.put("name", com.getName());
        map.put("companyId", com.getCompanyId());
        map.put("code", com.getCode());
        map.put("serverId", com.getServerId());

        return new AuditApplicationEvent(new Date(), SecurityUtils.getCurrentUserLogin(), type, map);
    }

    /**
     * PUT  /smart-meters : Updates an existing smartMeter.
     *
     * @param smartMeterDTO the smartMeterDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartMeterDTO,
     * or with status 400 (Bad Request) if the smartMeterDTO is not valid,
     * or with status 500 (Internal Server Error) if the smartMeterDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smart-meters")
    @Timed
    public ResponseEntity<SmartMeterDTO> updateSmartMeter(@Valid @RequestBody SmartMeterDTO smartMeterDTO) throws URISyntaxException {
        log.debug("REST request to update SmartMeter : {}", smartMeterDTO);
        if (smartMeterDTO.getId() == null) {
            return createSmartMeter(smartMeterDTO);
        }
        Optional<SmartMeter> old = smartMeterRepository.findOneByCode(smartMeterDTO.getServerId(), smartMeterDTO.getCode());
        if(old.isPresent() && !old.get().getId().equals(smartMeterDTO.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modbusServer", "codeexists", "A smartMeter cannot already have a code")).body(null);
        }
        SmartMeter smartMeter = smartMeterMapper.smartMeterDTOToSmartMeter(smartMeterDTO);
        smartMeter = smartMeterRepository.save(smartMeter);
        ModbusReloadEvent reload = new ModbusReloadEvent(smartMeter);
        reload.setOperator(ModbusReloadEvent.OPERATOR_MODIFY);
        reload.setType(ModbusReloadEvent.MODBUS_METER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(smartMeter, UPDATE));
        SmartMeterDTO result = smartMeterMapper.smartMeterToSmartMeterDTO(smartMeter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("smartMeter", smartMeterDTO.getName()))
            .body(result);
    }

    /**
     * GET  /smart-meters : get all the smartMeters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartMeters in body
     */
    @GetMapping("/smart-meters")
    @Timed
    public List<SmartMeterDTO> getAllSmartMeters() {
        log.debug("REST request to get all SmartMeters");
        List<SmartMeter> smartMeters = smartMeterRepository.findAll();
        return smartMeterMapper.smartMetersToSmartMeterDTOs(smartMeters);
    }

    /**
     * GET  /smart-meters : get all the smartMeters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartMeters in body
     */
    @GetMapping("/smart-meters/query/{serverId}/{companyId}")
    @Timed
    public List<SmartMeterDTO> getSmartMetersByServerId(@PathVariable String serverId, @PathVariable String companyId) {
    	log.debug("REST request to get SmartMeters : {}", serverId);
    	List<SmartMeter> smartMeters = smartMeterRepository.findByServerId(UUID.fromString(serverId));
    	ModbusServer server = modbusServerRepository.findOneById(UUID.fromString(serverId)).get();
    	for (SmartMeter smartMeter : smartMeters) {
    		smartMeter.setServer(server);
    		try{
                Optional<SmartMeterStatus> status = smartMeterStatusRepository.findOneByName(smartMeter.getId(), LocalDateTime.now(), LocalDateTime.now());
    		    if(status.isPresent()) {
                    smartMeter.setStatus(status.get());
                }
	    	 }catch(Exception e){
	 			log.debug("SmartMeter status is not found: {}", e.getLocalizedMessage());
	 		}
		}
    	return smartMeterMapper.smartMetersToSmartMeterDTOs(smartMeters);
    }

    /**
     * GET  /smart-meters/:id : get the "id" smartMeter.
     *
     * @param id the id of the smartMeterDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartMeterDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smart-meters/{id}/{serverId}")
    @Timed
    public ResponseEntity<SmartMeterDTO> getSmartMeter(@PathVariable String id, @PathVariable String serverId) {
        log.debug("REST request to get SmartMeter : {}, {}", id, serverId);

        SmartMeter smartMeter = smartMeterRepository.findOneById(UUID.fromString(id)).get(); //smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));
        ModbusServer server = modbusServerRepository.findOneById(UUID.fromString(serverId)).get();
        smartMeter.setServer(server);
        try{
        	smartMeter.setStatus(smartMeterStatusRepository.findOneByName(smartMeter.getId(), LocalDateTime.now(), LocalDateTime.now()).get());
    	}catch(Exception e){
			log.debug("SmartMeter status is not found: {}", e.getLocalizedMessage());
		}
        SmartMeterDTO smartMeterDTO = smartMeterMapper.smartMeterToSmartMeterDTO(smartMeter);
        return Optional.ofNullable(smartMeterDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /smart-meters/:id : delete the "id" smartMeter.
     *
     * @param id the id of the smartMeterDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smart-meters/{id}/{serverId}")
    @Timed
    public ResponseEntity<Void> deleteSmartMeter(@PathVariable String id, @PathVariable String serverId) {
        log.debug("REST request to delete SmartMeter : {}, {}", id, serverId);
        SmartMeter meter = smartMeterRepository.findOneById(UUID.fromString(id)).get();// smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));
        smartMeterRepository.delete(UUID.fromString(id), UUID.fromString(serverId), meter.getCode());
        ModbusReloadEvent reload = new ModbusReloadEvent(meter);
        reload.setOperator(ModbusReloadEvent.OPERATOR_DELETE);
        reload.setType(ModbusReloadEvent.MODBUS_METER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(meter, DEL));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("smartMeter", meter.getName())).build();
    }

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
        SmartMeter meter = smartMeterRepository.findOneById(UUID.fromString(id)).get(); //smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));

        ModbusServer server = modbusServerRepository.findOneById(meter.getServerId()).get();
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
        SmartMeter meter = smartMeterRepository.findOneById(UUID.fromString(id)).get(); //smartMeterRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));
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
        ModbusServer server = modbusServerRepository.findOneById(meter.getServerId()).get();
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
