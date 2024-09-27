package com.example.productservice.Service.ElasticSearch;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Product.ProductRequest;

public interface ProductEService {

    // add
    void addProduct(Product product);
    // updateProductBasic
    void updateProductBasic(ProductRequest productRequest);
    // elasticsearch
    void updateProductDetail(ProductRequest productRequest);
    // elasticsearch
    void updateProductSell(Product product);
    // elasticsearch
    void updateProductShip(ProductRequest productRequest);
    // addReview
    void addReview(ProductReview reviewRequest);

    void plusView(String id);

    // update updateIsPublic
    void updateIsPublic(String data);

    // deleteSizeQuantityById
    void deleteSizeQuantityById(String data);

    // plus stock not size
    void plusStockNotSize(String data);

    // minus stock not size
    void minusStockNotSize(String data);

    
}
