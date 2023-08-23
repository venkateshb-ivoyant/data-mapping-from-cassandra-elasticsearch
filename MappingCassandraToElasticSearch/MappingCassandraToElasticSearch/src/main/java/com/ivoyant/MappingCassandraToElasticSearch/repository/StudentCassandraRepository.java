package com.ivoyant.MappingCassandraToElasticSearch.repository;

import com.ivoyant.MappingCassandraToElasticSearch.entity.Student;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentCassandraRepository extends CassandraRepository<Student, UUID> {
}
