package com.example.productservice.controller;

import com.example.productservice.Product.CategoryRequest;
import com.example.productservice.Entity.Category;
import com.example.productservice.Reponse.Category.CategoryListReponse;
import com.example.productservice.Reponse.Category.CategorySingleReponse;
import com.example.productservice.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/category")

public class CategoryController {

    @Autowired
    private  CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> findAllCategory() {
        List<Category> categories = categoryService.findAllCategory();
        CategoryListReponse categoryListReponse = new CategoryListReponse("Lấy categories thành công", categories);
        return new ResponseEntity<>(categoryListReponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        CategorySingleReponse categorySingleReponse = new CategorySingleReponse("OK", category);
        return new ResponseEntity<>(categorySingleReponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(categoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<String> handleCategoryRequest(@RequestBody List<String> categoryRequestList) {
        String response = categoryService.addCategoryList(categoryRequestList);
        return ResponseEntity.ok(response);
    }



}
