package com.example.productservice.Entity.ElasticSearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewE {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long idCustomer;

    @Field(type = FieldType.Integer)
    private int rating;

    @Field(type = FieldType.Text)
    private String comment;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;


}
