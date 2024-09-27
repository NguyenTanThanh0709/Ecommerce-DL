package com.example.cartservice.Dto;

import com.example.cartservice.Dto.Order_Cart.ProductReponseCart_Order;
import com.example.cartservice.Dto.Order_Cart.SizeQuantityReponseCart_Order;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartData {
    private String _id;
    private int buy_count;
    private double price;
    private double price_before_discount;
    private int status;
    private String user;
    private String createdAt;
    private String updatedAt;
    private Long id_size_quantity_color;

    private ProductReponseCart_Order product;
}
