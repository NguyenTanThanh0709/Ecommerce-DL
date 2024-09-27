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

public class ProductMapper {

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
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
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

    private static List<ProductReviewE> mapReviews(List<ProductReview> reviews) {
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
}
