package com.example.productservice.Mapper;

import com.example.productservice.Entity.ElasticSearch.ProductReviewE;
import com.example.productservice.Entity.ProductReview;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReviewMapper {
    @Autowired
    private ModelMapper modelMapper;

    public  ProductReview toProductReviewJPA(ProductReviewE productReviewE){
        return modelMapper.map(productReviewE, ProductReview.class);
    }

    public  ProductReviewE toProductReviewElas(ProductReview productReview){

        return modelMapper.map(productReview, ProductReviewE.class);
    }
}
