package com.example.productservice.Service;

import com.example.productservice.Product.CategoryRequest;
import com.example.productservice.Entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategory();
    Category getById(Long id);
    Category addCategory(CategoryRequest categoryRequest);

    // elasticsearch
    String addCategoryList(List<String> data);
}
