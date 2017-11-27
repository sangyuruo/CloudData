package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.domain.ServerStatus;
import com.distribution.data.repository.ServerStatusRepository;
import com.distribution.data.service.dto.ServerStatusDTO;
import com.distribution.data.service.mapper.ServerStatusMapper;
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
 * REST controller for managing ServerStatus.
 */
@RestController
@RequestMapping("/api")
public class ServerStatusResource {

    private final Logger log = LoggerFactory.getLogger(ServerStatusResource.class);

    @Inject
    private ServerStatusRepository serverStatusRepository;

    @Inject
    private ServerStatusMapper serverStatusMapper;

    /**
     * POST  /server-statuses : Create a new serverStatus.
     *
     * @param serverStatusDTO the serverStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serverStatusDTO, or with status 400 (Bad Request) if the serverStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/server-statuses")
    @Timed
    public ResponseEntity<ServerStatusDTO> createServerStatus(@Valid @RequestBody ServerStatusDTO serverStatusDTO) throws URISyntaxException {
        log.debug("REST request to save ServerStatus : {}", serverStatusDTO);
        if (serverStatusDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serverStatus", "idexists", "A new serverStatus cannot already have an ID")).body(null);
        }
        ServerStatus serverStatus = serverStatusMapper.serverStatusDTOToServerStatus(serverStatusDTO);
        serverStatus = serverStatusRepository.save(serverStatus);
        ServerStatusDTO result = serverStatusMapper.serverStatusToServerStatusDTO(serverStatus);
        return ResponseEntity.created(new URI("/api/server-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serverStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /server-statuses : Updates an existing serverStatus.
     *
     * @param serverStatusDTO the serverStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serverStatusDTO,
     * or with status 400 (Bad Request) if the serverStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the serverStatusDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/server-statuses")
    @Timed
    public ResponseEntity<ServerStatusDTO> updateServerStatus(@Valid @RequestBody ServerStatusDTO serverStatusDTO) throws URISyntaxException {
        log.debug("REST request to update ServerStatus : {}", serverStatusDTO);
        if (serverStatusDTO.getId() == null) {
            return createServerStatus(serverStatusDTO);
        }
        ServerStatus serverStatus = serverStatusMapper.serverStatusDTOToServerStatus(serverStatusDTO);
        serverStatus = serverStatusRepository.save(serverStatus);
        ServerStatusDTO result = serverStatusMapper.serverStatusToServerStatusDTO(serverStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serverStatus", serverStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /server-statuses : get all the serverStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serverStatuses in body
     */
    @GetMapping("/server-statuses")
    @Timed
    public List<ServerStatusDTO> getAllServerStatuses() {
        log.debug("REST request to get all ServerStatuses");
        List<ServerStatus> serverStatuses = serverStatusRepository.findAll();
        return serverStatusMapper.serverStatusesToServerStatusDTOs(serverStatuses);
    }

    /**
     * GET  /server-statuses/:id : get the "id" serverStatus.
     *
     * @param id the id of the serverStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serverStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/server-statuses/{id}/{serverId}")
    @Timed
    public ResponseEntity<ServerStatusDTO> getServerStatus(@PathVariable String id, @PathVariable String serverId) {
        log.debug("REST request to get ServerStatus : {},{}", id, serverId);
        ServerStatus serverStatus = serverStatusRepository.findOne(UUID.fromString(id), UUID.fromString(serverId));
        ServerStatusDTO serverStatusDTO = serverStatusMapper.serverStatusToServerStatusDTO(serverStatus);
        return Optional.ofNullable(serverStatusDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /server-statuses/:id : delete the "id" serverStatus.
     *
     * @param id the id of the serverStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/server-statuses/{id}/{serverId}")
    @Timed
    public ResponseEntity<Void> deleteServerStatus(@PathVariable String id, @PathVariable String serverId) {
        log.debug("REST request to delete ServerStatus : {}, {}", id, serverId);
        serverStatusRepository.delete(UUID.fromString(id), UUID.fromString(serverId));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serverStatus", id.toString())).build();
    }

}
