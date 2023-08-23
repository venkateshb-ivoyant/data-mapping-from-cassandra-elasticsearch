package com.ivoyant.MappingCassandraToElasticSearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ivoyant.MappingCassandraToElasticSearch.repository.StudentElasticSearchRepository")
public class ElasticSearchConfig {

   @Bean
   public RestHighLevelClient elasticsearchClient() {
       return new RestHighLevelClient(
               RestClient.builder(                        new HttpHost("localhost", 9200, "http") // Adjust host and port as needed
                        // You can configure more hosts if you have a cluster setup
                )
        );
    }
   /* @Bean
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }*/
}


