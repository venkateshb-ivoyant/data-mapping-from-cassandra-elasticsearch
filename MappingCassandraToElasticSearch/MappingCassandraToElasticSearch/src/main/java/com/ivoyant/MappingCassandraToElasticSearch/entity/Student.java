package com.ivoyant.MappingCassandraToElasticSearch.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.elasticsearch.annotations.Document;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Table("students")

public class Student implements Serializable {
    @Id
    @PrimaryKey
    private UUID id = Uuids.timeBased();
    @Column("student_name")
    private String name;
    private Integer grade;
    @Column("address")
    private AddressUDT address;
    @Column("subjects")
    private List<SubjectUDT> subjects;
}
