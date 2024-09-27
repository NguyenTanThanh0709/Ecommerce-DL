package com.example.productservice.Service;

import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Reponse.ProductReviewRequest;

import java.util.List;

public interface ProductReviewService {

    // elasticsearch
    public ProductReview addReview(ProductReviewRequest reviewRequest);
    
    List<ProductReview> getReviewsByProductId(Long productId);
}
