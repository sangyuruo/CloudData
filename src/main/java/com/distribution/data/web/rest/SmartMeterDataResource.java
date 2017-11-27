package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.domain.SmartMeterData;
import com.distribution.data.repository.SmartMeterDataRepository;
import com.distribution.data.service.dto.SmartMeterDataDTO;
import com.distribution.data.service.mapper.SmartMeterDataMapper;
import com.distribution.data.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing SmartMeterData.
 */
@RestController
@RequestMapping("/api")
public class SmartMeterDataResource {

    private final Logger log = LoggerFactory.getLogger(SmartMeterDataResource.class);

    @Inject
    private SmartMeterDataRepository smartMeterDataRepository;

    @Inject
    private SmartMeterDataMapper smartMeterDataMapper;

    /**
     * POST  /smart-meter-data : Create a new smartMeterData.
     *
     * @param smartMeterDataDTO the smartMeterDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartMeterDataDTO, or with status 400 (Bad Request) if the smartMeterData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smart-meter-data")
    @Timed
    public ResponseEntity<SmartMeterDataDTO> createSmartMeterData(@Valid @RequestBody SmartMeterDataDTO smartMeterDataDTO) throws URISyntaxException {
        log.debug("REST request to save SmartMeterData : {}", smartMeterDataDTO);
        if (smartMeterDataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("smartMeterData", "idexists", "A new smartMeterData cannot already have an ID")).body(null);
        }
        SmartMeterData smartMeterData = smartMeterDataMapper.smartMeterDataDTOToSmartMeterData(smartMeterDataDTO);
        smartMeterData = smartMeterDataRepository.save(smartMeterData);
        SmartMeterDataDTO result = smartMeterDataMapper.smartMeterDataToSmartMeterDataDTO(smartMeterData);
        return ResponseEntity.created(new URI("/api/smart-meter-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("smartMeterData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smart-meter-data : Updates an existing smartMeterData.
     *
     * @param smartMeterDataDTO the smartMeterDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartMeterDataDTO,
     * or with status 400 (Bad Request) if the smartMeterDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the smartMeterDataDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smart-meter-data")
    @Timed
    public ResponseEntity<SmartMeterDataDTO> updateSmartMeterData(@Valid @RequestBody SmartMeterDataDTO smartMeterDataDTO) throws URISyntaxException {
        log.debug("REST request to update SmartMeterData : {}", smartMeterDataDTO);
        if (smartMeterDataDTO.getId() == null) {
            return createSmartMeterData(smartMeterDataDTO);
        }
        SmartMeterData smartMeterData = smartMeterDataMapper.smartMeterDataDTOToSmartMeterData(smartMeterDataDTO);
        smartMeterData = smartMeterDataRepository.save(smartMeterData);
        SmartMeterDataDTO result = smartMeterDataMapper.smartMeterDataToSmartMeterDataDTO(smartMeterData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("smartMeterData", smartMeterDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smart-meter-data : get all the smartMeterData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartMeterData in body
     */
    @GetMapping("/smart-meter-data")
    @Timed
    public List<SmartMeterDataDTO> getAllSmartMeterData() {
        log.debug("REST request to get all SmartMeterData");
        List<SmartMeterData> smartMeterData = smartMeterDataRepository.findAll();
        return smartMeterDataMapper.smartMeterDataToSmartMeterDataDTOs(smartMeterData);
    }

    /**
     * GET  /smart-meter-data/:id : get the "id" smartMeterData.
     *
     * @param id the id of the smartMeterDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartMeterDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smart-meter-data/{id}/{meterId}/{companyId}")
    @Timed
    public ResponseEntity<SmartMeterDataDTO> getSmartMeterData(@PathVariable String id, @PathVariable String meterId, @PathVariable String companyId) {
        log.debug("REST request to get SmartMeterData : {}, {}", id, meterId);
        SmartMeterData smartMeterData = smartMeterDataRepository.findOne(UUID.fromString(id), UUID.fromString(meterId), UUID.fromString(companyId));
        SmartMeterDataDTO smartMeterDataDTO = smartMeterDataMapper.smartMeterDataToSmartMeterDataDTO(smartMeterData);
        return Optional.ofNullable(smartMeterDataDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /smart-meter-data/:id : delete the "id" smartMeterData.
     *
     * @param id the id of the smartMeterDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smart-meter-data/{id}/{meterId}/{companyId}")
    @Timed
    public ResponseEntity<Void> deleteSmartMeterData(@PathVariable String id, @PathVariable String meterId, @PathVariable String companyId) {
        log.debug("REST request to delete SmartMeterData : {}, {}", id, meterId);
        smartMeterDataRepository.delete(UUID.fromString(id), UUID.fromString(meterId), UUID.fromString(companyId));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("smartMeterData", id.toString())).build();
    }

}
