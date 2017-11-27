package com.distribution.data.repository.search;

import com.distribution.data.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, String> {
}
