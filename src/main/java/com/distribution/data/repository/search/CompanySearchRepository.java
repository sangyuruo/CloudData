package com.distribution.data.repository.search;

import com.distribution.data.domain.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, String> {
}
