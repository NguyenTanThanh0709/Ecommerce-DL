package com.example.productservice.Reponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductReponse {
    private String message;
    private ProductData data;
}