package com.example.productservice.controller;

import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Reponse.ProductReviewRequest;
import com.example.productservice.Service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/review")
public class ProductReviewController {

    @Autowired
    private ProductReviewService productReviewService;

    @PostMapping("/")
    public ResponseEntity<ProductReview> addReview(@RequestBody ProductReviewRequest reviewRequest) {
        ProductReview review = productReviewService.addReview(reviewRequest);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview>> getReviewsByProductId(@PathVariable Long productId) {
        List<ProductReview> reviews = productReviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }
}
