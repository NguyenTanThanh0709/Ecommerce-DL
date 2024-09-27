package com.example.productservice.Reponse;

import com.example.productservice.Entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewRequest {
    private Long id;
    private Long idCustomer;
    private int rating;
    private String comment;
    private Long productId;
}
