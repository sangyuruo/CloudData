package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.collector.event.ModbusReloadEvent;
import com.distribution.data.domain.ModbusServer;
import com.distribution.data.repository.CompanyRepository;
import com.distribution.data.repository.ModbusServerRepository;
import com.distribution.data.repository.ServerStatusRepository;
import com.distribution.data.service.dto.ModbusServerDTO;
import com.distribution.data.service.mapper.ModbusServerMapper;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * REST controller for managing ModbusSlave.
 */
@RestController
@RequestMapping("/api")
public class ModbusServerResource implements ApplicationEventPublisherAware {
    public static final String ADD = "MODBUSSERVER_ADD";
    public static final String UPDATE = "MODBUSSERVER_UPDATE";
    public static final String DEL = "MODBUSSERVER_DELETE";
    private final Logger log = LoggerFactory.getLogger(ModbusServerResource.class);
    private ApplicationEventPublisher eventPublisher;
    @Inject
    private ModbusServerRepository modbusServerRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private ModbusServerMapper modbusServerMapper;

    @Inject
    private ServerStatusRepository serverStatusRepository;

    /**
     * POST  /modbus-servers : Create a new modbusServer.
     *
     * @param modbusServerDTO the modbusServerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modbusServerDTO, or with status 400 (Bad Request) if the modbusServer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/modbus-servers")
    @Timed
    public ResponseEntity<ModbusServerDTO> createModbusServer(@Valid @RequestBody ModbusServerDTO modbusServerDTO) throws URISyntaxException {
        log.debug("REST request to save ModbusSlave : {}", modbusServerDTO);
        if (modbusServerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modbusServer", "idexists", "A new modbusServer cannot already have an ID")).body(null);
        }
        Optional<ModbusServer> old = modbusServerRepository.findOneByCode(modbusServerDTO.getCode());
        if(old.isPresent()){
             return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modbusServer", "codeexists", "A new modbusServer cannot already have a code")).body(null);
        }
        ModbusServer modbusServer = modbusServerMapper.modbusServerDTOToModbusServer(modbusServerDTO);
        modbusServer = modbusServerRepository.save(modbusServer);
        ModbusReloadEvent reload = new ModbusReloadEvent(modbusServer);
        reload.setOperator(ModbusReloadEvent.OPERATOR_ADD);
        reload.setType(ModbusReloadEvent.MODBUS_SERVER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(modbusServer, ADD));
        ModbusServerDTO result = modbusServerMapper.modbusServerToModbusServerDTO(modbusServer);
        return ResponseEntity.created(new URI("/api/modbus-servers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("modbusServer", result.getHostname()))
            .body(result);
    }

    private AuditApplicationEvent createAudit(ModbusServer com, String type){
        Map<String, Object> map = new HashMap<>();
        map.put("id", com.getId());
        map.put("hostname", com.getHostname());
        map.put("code", com.getCode());
        map.put("companyId", com.getCompanyId());
        return new AuditApplicationEvent(new Date(), SecurityUtils.getCurrentUserLogin(), type, map);
    }

    /**
     * PUT  /modbus-servers : Updates an existing modbusServer.
     *
     * @param modbusServerDTO the modbusServerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modbusServerDTO,
     * or with status 400 (Bad Request) if the modbusServerDTO is not valid,
     * or with status 500 (Internal Server Error) if the modbusServerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/modbus-servers")
    @Timed
    public ResponseEntity<ModbusServerDTO> updateModbusServer(@Valid @RequestBody ModbusServerDTO modbusServerDTO) throws URISyntaxException {
        log.debug("REST request to update ModbusSlave : {}", modbusServerDTO);
        if (modbusServerDTO.getId() == null) {
            return createModbusServer(modbusServerDTO);
        }

        Optional<ModbusServer> old = modbusServerRepository.findOneByCode(modbusServerDTO.getCode());
        if(old.isPresent() && !old.get().getId().equals(modbusServerDTO.getId())){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modbusServer", "codeexists", "A modbusServer cannot already have a code")).body(null);
        }
        ModbusServer modbusServer = modbusServerMapper.modbusServerDTOToModbusServer(modbusServerDTO);
        modbusServer = modbusServerRepository.save(modbusServer);
        ModbusReloadEvent reload = new ModbusReloadEvent(modbusServer);
        reload.setOperator(ModbusReloadEvent.OPERATOR_MODIFY);
        reload.setType(ModbusReloadEvent.MODBUS_SERVER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(modbusServer, UPDATE));
        ModbusServerDTO result = modbusServerMapper.modbusServerToModbusServerDTO(modbusServer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("modbusServer", modbusServerDTO.getHostname()))
            .body(result);
    }

    /**
     * GET  /modbus-servers : get all the modbusServers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modbusServers in body
     */
    @GetMapping("/modbus-servers")
    @Timed
    public List<ModbusServerDTO> getAllModbusServers() {
        log.debug("REST request to get all ModbusServers");
        List<ModbusServer> modbusServers = modbusServerRepository.findAll();
        for (ModbusServer modbusServer : modbusServers) {
        	modbusServer.setCompany(companyRepository.findOne(modbusServer.getCompanyId()));
        	try{
        		modbusServer.setStatus(serverStatusRepository.findOneByName(modbusServer.getId(), LocalDateTime.now(), LocalDateTime.now()).get());
        	}catch(Exception e){
        		log.debug("ModbusSlave status is not found: {}", e.getLocalizedMessage());
        	}
		}
        return modbusServerMapper.modbusServersToModbusServerDTOs(modbusServers);
    }

    /**
     * GET  /modbus-servers/:id : get the "id" modbusServer.
     *
     * @param id the id of the modbusServerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modbusServerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/modbus-servers/{id}/{companyId}")
    @Timed
    public ResponseEntity<ModbusServerDTO> getModbusServer(@PathVariable String id, @PathVariable String companyId) {
        log.debug("REST request to get ModbusSlave : {}, {}", id, companyId);
        ModbusServer modbusServer = modbusServerRepository.findOne(UUID.fromString(id), UUID.fromString(companyId));
        modbusServer.setCompany(companyRepository.findOne(modbusServer.getCompanyId()));
        try{
        	modbusServer.setStatus(serverStatusRepository.findOneByName(modbusServer.getId(), LocalDateTime.now(), LocalDateTime.now()).get());
	    }catch(Exception e){
			log.debug("ModbusSlave status is not found: {}", e.getLocalizedMessage());
		}
        ModbusServerDTO modbusServerDTO = modbusServerMapper.modbusServerToModbusServerDTO(modbusServer);
        return Optional.ofNullable(modbusServerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /modbus-servers/:id : delete the "id" modbusServer.
     *
     * @param id the id of the modbusServerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/modbus-servers/{id}/{companyId}")
    @Timed
    public ResponseEntity<Void> deleteModbusServer(@PathVariable String id, @PathVariable String companyId) {
        log.debug("REST request to delete ModbusSlave : {}, {}", id, companyId);
        ModbusServer ms = modbusServerRepository.findOne(UUID.fromString(id), UUID.fromString(companyId));
        modbusServerRepository.delete(UUID.fromString(id), UUID.fromString(companyId));
        ModbusReloadEvent reload = new ModbusReloadEvent(ms);
        reload.setOperator(ModbusReloadEvent.OPERATOR_DELETE);
        reload.setType(ModbusReloadEvent.MODBUS_SERVER);
        this.eventPublisher.publishEvent(reload);
        this.eventPublisher.publishEvent(createAudit(ms, DEL));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("modbusServer", ms.getHostname())).build();
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher =  applicationEventPublisher;
	}
}
