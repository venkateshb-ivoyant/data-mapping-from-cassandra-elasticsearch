package com.ivoyant.MappingCassandraToElasticSearch.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.data.elasticsearch.annotations.Document;


import java.io.Serializable;

@Data
@UserDefinedType("subject")
@Document(indexName = "subject")
public class SubjectUDT  implements Serializable {
    private String name;
}
