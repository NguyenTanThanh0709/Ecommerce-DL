package com.example.productservice.Reponse.Order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDataRequest {
    private Long id;               // Unique identifier for the order item
    private Long productId;        // Identifier for the product
    private Long idSizeQuantity;   // Identifier for the size and quantity
    private Long promotionId;      // Identifier for any promotion applied
    private Integer quantity;       // Quantity of the product in the order
    private String note;
}
