package com.example.productservice.Product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeQuantityRequest {
    private Long id;
    private String size;
    private String color;
    private int quantity;
}