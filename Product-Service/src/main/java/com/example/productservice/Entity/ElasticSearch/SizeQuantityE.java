package com.example.productservice.Entity.ElasticSearch;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeQuantityE {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String size;

    @Field(type = FieldType.Text)
    private String color;

    @Field(type = FieldType.Integer)
    private int quantity;

}
