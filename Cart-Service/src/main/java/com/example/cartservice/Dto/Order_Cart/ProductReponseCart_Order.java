package com.example.cartservice.Dto.Order_Cart;

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
    private double height;
    private double weight;
    private CategoryRequest category;
    private String createdAt;
    private String updatedAt;
    private double rating = 0;
    private int sold = 0;
    private int view = 0;
    private int orderNumber = 0;
    private List<SizeQuantityReponseCart_Order> sizeQuantities;
    private boolean isPublic = true;
}
