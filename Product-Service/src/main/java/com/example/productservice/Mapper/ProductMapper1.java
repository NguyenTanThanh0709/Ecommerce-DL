package com.example.productservice.Mapper;

import com.example.productservice.Entity.ElasticSearch.CategoryE;
import com.example.productservice.Entity.ElasticSearch.ProductE;
import com.example.productservice.Entity.ElasticSearch.ProductReviewE;
import com.example.productservice.Entity.ElasticSearch.SizeQuantityE;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.productservice.Entity.Category;
import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Entity.SizeQuantity;

public class ProductMapper1 {

    public static ProductE toProductElas (Product product){
        return ProductE.builder()
                .id(product.getId())
                .images(product.getImages())
                .price(product.getPrice())
                .priceBeforeDiscount(product.getPriceBeforeDiscount())
                .quantity(product.getQuantity())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .image(product.getImage())
                .idShop(product.getIdShop())
                .length(product.getLength())
                .width(product.getWidth())
                .height(product.getHeight())
                .weight(product.getWeight())
                // .createdAt(product.getCreatedAt())
                // .updatedAt(product.getUpdatedAt())
                .rating(product.getRating())
                .sold(product.getSold())
                .view(product.getView())
                .orderNumber(product.getOrderNumber())
                .isPublic(product.isPublic())
                .sizeQuantities(mapSizeQuantities(product.getSizeQuantities()))
                .category(mapCategory(product.getCategory()))
                .reviews(mapReviews(product.getReviews()))
                .build();
    }


     public static List<SizeQuantityE> mapSizeQuantities(List<SizeQuantity> sizeQuantities) {
        return sizeQuantities == null ? null : sizeQuantities.stream()
                .map(sizeQuantity -> SizeQuantityE.builder()
                        .id(sizeQuantity.getId())
                        .size(sizeQuantity.getSize())
                        .quantity(sizeQuantity.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private static CategoryE mapCategory(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryE.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<ProductReviewE> mapReviews(List<ProductReview> reviews) {
        return reviews == null ? Collections.emptyList() : reviews.stream()
                .map(review -> ProductReviewE.builder()
                        .id(review.getId())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .build())
                .collect(Collectors.toList());
    }
    

    public static ProductReviewE mapReview(ProductReview review) {
        if (review == null) {
            return null;
        }
        return ProductReviewE.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }



    public static Product toProduct(ProductE productE) {
        return Product.builder()
                .id(productE.getId())
                .images(productE.getImages())
                .price(productE.getPrice())
                .priceBeforeDiscount(productE.getPriceBeforeDiscount())
                .quantity(productE.getQuantity())
                .name(productE.getName())
                .description(productE.getDescription())
                .shortDescription(productE.getShortDescription())
                .image(productE.getImage())
                .idShop(productE.getIdShop())
                .length(productE.getLength())
                .width(productE.getWidth())
                .height(productE.getHeight())
                .weight(productE.getWeight())
                // .createdAt(productE.getCreatedAt())
                // .updatedAt(productE.getUpdatedAt())
                .rating(productE.getRating())
                .sold(productE.getSold())
                .view(productE.getView())
                .orderNumber(productE.getOrderNumber())
                .isPublic(productE.isPublic())
                .sizeQuantities(mapSizeQuantitiesEToSizeQuantities(productE.getSizeQuantities()))
                .category(mapCategoryEToCategory(productE.getCategory()))
                .reviews(mapReviewsEToReviews(productE.getReviews()))
                .build();
    }

    // Reverse mapping for SizeQuantityE to SizeQuantity
    public static List<SizeQuantity> mapSizeQuantitiesEToSizeQuantities(List<SizeQuantityE> sizeQuantitiesE) {
        return sizeQuantitiesE == null ? null : sizeQuantitiesE.stream()
                .map(sizeQuantityE -> SizeQuantity.builder()
                        .id(sizeQuantityE.getId())
                        .size(sizeQuantityE.getSize())
                        .quantity(sizeQuantityE.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    // Reverse mapping for CategoryE to Category
    private static Category mapCategoryEToCategory(CategoryE categoryE) {
        if (categoryE == null) {
            return null;
        }
        return Category.builder()
                .id(categoryE.getId())
                .name(categoryE.getName())
                .build();
    }

    // Reverse mapping for ProductReviewE to ProductReview
    private static List<ProductReview> mapReviewsEToReviews(List<ProductReviewE> reviewsE) {
        return reviewsE == null ? Collections.emptyList() : reviewsE.stream()
                .map(reviewE -> ProductReview.builder()
                        .id(reviewE.getId())
                        .rating(reviewE.getRating())
                        .comment(reviewE.getComment())
                        .build())
                .collect(Collectors.toList());
    }

    // Reverse mapping for single ProductReviewE to ProductReview
    public static ProductReview mapReviewEToReview(ProductReviewE reviewE) {
        if (reviewE == null) {
            return null;
        }
        return ProductReview.builder()
                .id(reviewE.getId())
                .rating(reviewE.getRating())
                .comment(reviewE.getComment())
                .build();
    }

}
