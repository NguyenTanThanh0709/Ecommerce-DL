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
public class CategoryE {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

}
