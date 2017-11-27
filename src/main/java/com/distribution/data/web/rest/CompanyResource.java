package com.distribution.data.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.distribution.data.domain.Company;
import com.distribution.data.repository.CompanyRepository;
import com.distribution.data.service.dto.CompanyDTO;
import com.distribution.data.service.mapper.CompanyMapper;
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
import java.util.*;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource implements ApplicationEventPublisherAware {

    public static final String ADD = "COMPANY_ADD";
    public static final String UPDATE = "COMPANY_UPDATE";
    public static final String DEL = "COMPANY_DELETE";

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private ApplicationEventPublisher eventPublisher;
    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyMapper companyMapper;

    /**
     * POST  /companies : Create a new company.
     *
     * @param companyDTO the companyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyDTO, or with status 400 (Bad Request) if the company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/companies")
    @Timed
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to save Company : {}", companyDTO);
        if (companyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("company", "idexists", "A new company cannot already have an ID")).body(null);
        }
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.companyToCompanyDTO(company);
        this.eventPublisher.publishEvent(createAudit(company, ADD));
        return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("company", result.getId().toString()))
            .body(result);
    }

    private AuditApplicationEvent createAudit(Company com, String type){
        Map<String, Object> map = new HashMap<>();
        map.put("id", com.getId());
        map.put("name", com.getName());
        return new AuditApplicationEvent(new Date(), SecurityUtils.getCurrentUserLogin(), type, map);
    }

    /**
     * PUT  /companies : Updates an existing company.
     *
     * @param companyDTO the companyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyDTO,
     * or with status 400 (Bad Request) if the companyDTO is not valid,
     * or with status 500 (Internal Server Error) if the companyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/companies")
    @Timed
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to update Company : {}", companyDTO);
        if (companyDTO.getId() == null) {
            return createCompany(companyDTO);
        }
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        company = companyRepository.save(company);
        CompanyDTO result = companyMapper.companyToCompanyDTO(company);
        this.eventPublisher.publishEvent(createAudit(company, UPDATE));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("company", companyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companies : get all the companies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companies in body
     */
    @GetMapping("/companies")
    @Timed
    public List<CompanyDTO> getAllCompanies() {
        log.debug("REST request to get all Companies");
        List<Company> companies = companyRepository.findAll();
        return companyMapper.companiesToCompanyDTOs(companies);
    }

    /**
     * GET  /companies/:id : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/companies/{id}")
    @Timed
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable String id) {
        log.debug("REST request to get Company : {}", id);
        Company company = companyRepository.findOne(UUID.fromString(id));
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);
        return Optional.ofNullable(companyDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companies/:id : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/companies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        log.debug("REST request to delete Company : {}", id);
        Company company = companyRepository.findOne(UUID.fromString(id));
        companyRepository.delete(UUID.fromString(id));
        this.eventPublisher.publishEvent(createAudit(company, DEL));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("company", id.toString())).build();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
