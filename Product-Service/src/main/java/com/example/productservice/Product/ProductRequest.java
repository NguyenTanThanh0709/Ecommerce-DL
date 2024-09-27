package com.example.productservice.Product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private Long id;

    private List<String> images;
    private String name;
    private String description;
    private Long categoryId;
    private String image = "";

    private String shortDescription;

    private double length;
    private double width;
    private double height;
    private double weight;

    private double price;
    private double priceBeforeDiscount;
    private int quantity = 0;
    private List<SizeQuantityRequest> sizeQuantities;

    private Long idShop;


    private String createdAt;
    private String updatedAt;
    private boolean isPublic = true;

}
