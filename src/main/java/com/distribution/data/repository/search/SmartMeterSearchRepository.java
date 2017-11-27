package com.distribution.data.repository.search;

import com.distribution.data.domain.SmartMeter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface SmartMeterSearchRepository extends ElasticsearchRepository<SmartMeter, String> {
}
