package com.distribution.data.repository.search;

import com.distribution.data.domain.ModbusServer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface ModbusServerSearchRepository extends ElasticsearchRepository<ModbusServer, String> {
}
