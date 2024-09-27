package com.example.productservice.Reponse;

import com.example.productservice.Entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductReponSingle {
    private String message;
    private Product data;
}