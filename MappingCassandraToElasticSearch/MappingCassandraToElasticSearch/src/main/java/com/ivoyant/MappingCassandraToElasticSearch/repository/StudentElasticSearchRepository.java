package com.ivoyant.MappingCassandraToElasticSearch.repository;

import com.ivoyant.MappingCassandraToElasticSearch.entity.ElasticStudent;
import com.ivoyant.MappingCassandraToElasticSearch.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface StudentElasticSearchRepository extends ElasticsearchRepository<ElasticStudent, UUID> {


}

