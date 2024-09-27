package com.example.productservice.Service.impl;

import com.example.productservice.Product.CategoryRequest;
import com.example.productservice.Entity.Category;
import com.example.productservice.Entity.ElasticSearch.CategoryE;
import com.example.productservice.Repository.CategoryRepository;
import com.example.productservice.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category addCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public String addCategoryList(List<String> data) {
        List<Category> categories = data.stream()
                .map(name -> Category.builder().name(name).build())
                .collect(Collectors.toList());
                 // Lưu danh sách Category vào cơ sở dữ liệu
    List<Category> savedCategories = categoryRepository.saveAll(categories);
        return "Categories added successfully";
    }
}
