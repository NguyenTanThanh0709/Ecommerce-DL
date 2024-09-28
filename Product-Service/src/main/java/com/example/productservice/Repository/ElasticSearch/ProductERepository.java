package com.example.productservice.Repository.ElasticSearch;

import com.example.productservice.Entity.ElasticSearch.ProductE;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
@Repository
public interface ProductERepository extends ElasticsearchRepository<ProductE, Long> {
    @Query("{ \"bool\": { " +
        "  \"must\": [ " +
        "    { \"term\": { \"isPublic\": true } }, " +
        "    { \"bool\": { \"should\": [ " +
        "      { \"wildcard\": { \"name.keyword\": \"*?0*\" } }, " +
        "      { \"term\": { \"category.id\": ?1 } } " +
        "    ]}}, " +
        "    { \"range\": { \"price\": { \"gte\": ?2, \"lte\": ?3 } } }, " +
        "    { \"range\": { \"rating\": { \"gte\": ?4 } } } " +
        "  ], " +
        "  \"filter\": { " +
        "    \"exists\": { \"field\": \"isPublic\" } " +
        "  } " +
        "} }," +
        "\"sort\": [ " +
        "  { \"view\": { \"order\": \"desc\" } }, " +
        "  { \"sold\": { \"order\": \"desc\" } }, " +
        "  { \"price\": { \"order\": ?5 } }" +
        "]}")
Page<ProductE> findAllWithFiltersAndSorting(String name, Long idcategory, Double price_min, Double price_max, Integer rating_filter, String order, Pageable pageable);

}
