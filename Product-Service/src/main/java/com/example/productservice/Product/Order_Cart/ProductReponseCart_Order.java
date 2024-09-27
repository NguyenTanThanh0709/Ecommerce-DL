package com.example.productservice.Product.Order_Cart;

import com.example.productservice.Product.CategoryRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReponseCart_Order {
    private Long id;
    private List<String> images;
    private double price;
    private double priceBeforeDiscount = 0;
    private int quantity;
    private String name;
    private String description;
    private String shortDescription;
    private String image;
    private Long idShop;
    private double length;
    private double width;
    private double hight;
    private double weight;
    private CategoryRequest category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double rating = 0;
    private int sold = 0;
    private int view = 0;
    private int orderNumber = 0;
    private boolean isPublic = true;
}
