package com.example.productservice.Reponse.Category;

import com.example.productservice.Entity.Category;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryListReponse {
    private String message;
    private List<Category> data;
}
