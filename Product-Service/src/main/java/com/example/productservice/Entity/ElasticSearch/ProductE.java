package com.example.productservice.Entity.ElasticSearch;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductE {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private List<String> images;

    @Field(type = FieldType.Double)
    private double price;

    @Field(type = FieldType.Double)
    private double priceBeforeDiscount;

    @Field(type = FieldType.Integer)
    private int quantity;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String shortDescription;

    @Field(type = FieldType.Keyword)
    private String image;

    @Field(type = FieldType.Long)
    private Long idShop;

    @Field(type = FieldType.Double)
    private double length;

    @Field(type = FieldType.Double)
    private double width;

    @Field(type = FieldType.Double)
    private double height;

    @Field(type = FieldType.Double)
    private double weight;

    @Field(type = FieldType.Text)
    private String createdAt = LocalDateTime.now().toString();

    @Field(type = FieldType.Text)
    private String updatedAt = LocalDateTime.now().toString();

    @Field(type = FieldType.Double)
    private double rating;

    @Field(type = FieldType.Integer)
    private int sold;

    @Field(type = FieldType.Integer)
    private int view;

    @Field(type = FieldType.Integer)
    private int orderNumber;

    @Field(type = FieldType.Boolean)
    private boolean isPublic;

    @Field(type = FieldType.Nested)
    private List<SizeQuantityE> sizeQuantities;

    @Field(type = FieldType.Object)
    private CategoryE category;

    @Field(type = FieldType.Nested)
    private List<ProductReviewE> reviews;

}
