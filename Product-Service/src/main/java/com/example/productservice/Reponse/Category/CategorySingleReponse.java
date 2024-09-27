package com.example.productservice.Reponse.Category;

import com.example.productservice.Entity.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategorySingleReponse {
    private String message;
    private Category data;
}
