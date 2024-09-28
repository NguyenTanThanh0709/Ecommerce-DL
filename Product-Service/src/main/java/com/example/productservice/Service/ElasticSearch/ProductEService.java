package com.example.productservice.Service.ElasticSearch;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Product.ProductRequest;

public interface ProductEService {

    // add
    void addProduct(Product product);
    // updateProductBasic
    void updateProductBasic(Product product);
    // elasticsearch
    void updateProductDetail(Product product);
    // elasticsearch
    void updateProductSell(Product product);
    // elasticsearch
    void updateProductShip(Product product);
    // addReview
    void addReview(Product product);

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
