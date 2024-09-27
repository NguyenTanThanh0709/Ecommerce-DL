package com.example.productservice.Reponse.ReponseOrder;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReponseOrder {
    private Long productId;
    private Long promotionId;
    private Long idSizeQuantity;
    private int quantity;
}
