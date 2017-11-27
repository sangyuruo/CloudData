package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.domain.SmartMeterStatus;
import com.distribution.data.repository.SmartMeterStatusRepository;
import com.distribution.data.service.dto.SmartMeterStatusDTO;
import com.distribution.data.service.mapper.SmartMeterStatusMapper;
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
 * REST controller for managing SmartMeterStatus.
 */
@RestController
@RequestMapping("/api")
public class SmartMeterStatusResource {

    private final Logger log = LoggerFactory.getLogger(SmartMeterStatusResource.class);

    @Inject
    private SmartMeterStatusRepository smartMeterStatusRepository;

    @Inject
    private SmartMeterStatusMapper smartMeterStatusMapper;

    /**
     * POST  /smart-meter-statuses : Create a new smartMeterStatus.
     *
     * @param smartMeterStatusDTO the smartMeterStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartMeterStatusDTO, or with status 400 (Bad Request) if the smartMeterStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smart-meter-statuses")
    @Timed
    public ResponseEntity<SmartMeterStatusDTO> createSmartMeterStatus(@Valid @RequestBody SmartMeterStatusDTO smartMeterStatusDTO) throws URISyntaxException {
        log.debug("REST request to save SmartMeterStatus : {}", smartMeterStatusDTO);
        if (smartMeterStatusDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("smartMeterStatus", "idexists", "A new smartMeterStatus cannot already have an ID")).body(null);
        }
        SmartMeterStatus smartMeterStatus = smartMeterStatusMapper.smartMeterStatusDTOToSmartMeterStatus(smartMeterStatusDTO);
        smartMeterStatus = smartMeterStatusRepository.save(smartMeterStatus);
        SmartMeterStatusDTO result = smartMeterStatusMapper.smartMeterStatusToSmartMeterStatusDTO(smartMeterStatus);
        return ResponseEntity.created(new URI("/api/smart-meter-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("smartMeterStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smart-meter-statuses : Updates an existing smartMeterStatus.
     *
     * @param smartMeterStatusDTO the smartMeterStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartMeterStatusDTO,
     * or with status 400 (Bad Request) if the smartMeterStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the smartMeterStatusDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smart-meter-statuses")
    @Timed
    public ResponseEntity<SmartMeterStatusDTO> updateSmartMeterStatus(@Valid @RequestBody SmartMeterStatusDTO smartMeterStatusDTO) throws URISyntaxException {
        log.debug("REST request to update SmartMeterStatus : {}", smartMeterStatusDTO);
        if (smartMeterStatusDTO.getId() == null) {
            return createSmartMeterStatus(smartMeterStatusDTO);
        }
        SmartMeterStatus smartMeterStatus = smartMeterStatusMapper.smartMeterStatusDTOToSmartMeterStatus(smartMeterStatusDTO);
        smartMeterStatus = smartMeterStatusRepository.save(smartMeterStatus);
        SmartMeterStatusDTO result = smartMeterStatusMapper.smartMeterStatusToSmartMeterStatusDTO(smartMeterStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("smartMeterStatus", smartMeterStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smart-meter-statuses : get all the smartMeterStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartMeterStatuses in body
     */
    @GetMapping("/smart-meter-statuses")
    @Timed
    public List<SmartMeterStatusDTO> getAllSmartMeterStatuses() {
        log.debug("REST request to get all SmartMeterStatuses");
        List<SmartMeterStatus> smartMeterStatuses = smartMeterStatusRepository.findAll();
        return smartMeterStatusMapper.smartMeterStatusesToSmartMeterStatusDTOs(smartMeterStatuses);
    }

    /**
     * GET  /smart-meter-statuses/:id : get the "id" smartMeterStatus.
     *
     * @param id the id of the smartMeterStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartMeterStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smart-meter-statuses/{id}/{meterId}")
    @Timed
    public ResponseEntity<SmartMeterStatusDTO> getSmartMeterStatus(@PathVariable String id, @PathVariable String meterId) {
        log.debug("REST request to get SmartMeterStatus : {}, {}", id, meterId);
        SmartMeterStatus smartMeterStatus = smartMeterStatusRepository.findOne(UUID.fromString(id), UUID.fromString(meterId));
        SmartMeterStatusDTO smartMeterStatusDTO = smartMeterStatusMapper.smartMeterStatusToSmartMeterStatusDTO(smartMeterStatus);
        return Optional.ofNullable(smartMeterStatusDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /smart-meter-statuses/:id : delete the "id" smartMeterStatus.
     *
     * @param id the id of the smartMeterStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smart-meter-statuses/{id}/{meterId}")
    @Timed
    public ResponseEntity<Void> deleteSmartMeterStatus(@PathVariable String id, @PathVariable String meterId) {
        log.debug("REST request to delete SmartMeterStatus : {}, {}", id, meterId);
        smartMeterStatusRepository.delete(UUID.fromString(id), UUID.fromString(meterId));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("smartMeterStatus", id.toString())).build();
    }

}
