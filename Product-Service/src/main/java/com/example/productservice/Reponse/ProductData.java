package com.example.productservice.Reponse;

import com.example.productservice.Entity.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductData {
    private List<Product> products;
    private Pagination pagination;
}