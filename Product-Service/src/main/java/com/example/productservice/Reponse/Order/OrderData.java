package com.example.productservice.Reponse.Order;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.Promotion;
import com.example.productservice.Entity.SizeQuantity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderData {
    private Long id;               // Unique identifier for the order item
    private Product product;        // Identifier for the product
    private Long idSizeQuantity;   // Identifier for the size and quantity
    private PromotionData promotion;      // Identifier for any promotion applied
    private Integer quantity;       // Quantity of the product in the order
    private String note;
}
