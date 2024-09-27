package com.example.productservice.Service.impl;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Reponse.ProductReviewRequest;
import com.example.productservice.Repository.ProductRepository;
import com.example.productservice.Repository.ProductReviewRepository;
import com.example.productservice.Service.ProducerService;
import com.example.productservice.Service.ProductReviewService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductReviewImpl implements ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProducerService<ProductReview> kafkaTemplateProductReview;
    @Override
    @Transactional
    public ProductReview addReview(ProductReviewRequest reviewRequest) {
        if (reviewRequest == null || reviewRequest.getProductId() == null) {
            throw new IllegalArgumentException("Review request or product ID cannot be null");
        }

        Optional<Product> productOpt = productRepository.findById(reviewRequest.getProductId());
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        Product product = productOpt.get();

        // Create new ProductReview
        ProductReview review = ProductReview.builder()
                .id(reviewRequest.getId())
                .idCustomer(reviewRequest.getIdCustomer())
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .product(product)
                .createdAt(LocalDateTime.now())
                .build();

        // Save the review
        ProductReview savedReview = productReviewRepository.save(review);

        // Calculate the new average rating
        List<ProductReview> reviews = productReviewRepository.findByProductId(product.getId());
        double averageRating = reviews.stream()
                .mapToInt(ProductReview::getRating)
                .average()
                .orElse(0.0);

        // Update the product's rating
        product.setRating(averageRating);
        productRepository.save(product);
        kafkaTemplateProductReview.sendSuperHeroMessage("addReview", savedReview);
        return savedReview;
    }

    @Override
    public List<ProductReview> getReviewsByProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        return productReviewRepository.findByProductId(productId);
    }
}
