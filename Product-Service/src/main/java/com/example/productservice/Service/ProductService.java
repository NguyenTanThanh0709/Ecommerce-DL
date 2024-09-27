package com.example.productservice.Service;

import com.example.productservice.Entity.SizeQuantity;
import com.example.productservice.Product.ProductRequest;
import com.example.productservice.Entity.Product;
import com.example.productservice.Reponse.Order.OrderData;
import com.example.productservice.Reponse.Order.OrderDataRequest;
import com.example.productservice.Reponse.Product_Promotion_SizeQuantityy_GET;
import com.example.productservice.Reponse.ReponseOrder.ReponseOrderData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    // elasticsearch
    Product addProduct(ProductRequest productRequest);
    // elasticsearch
    Product updateProductBasic(Long id, ProductRequest productRequest);
    // elasticsearch
    Product updateProductDetail(Long id, ProductRequest productRequest);
    // elasticsearch
    Product updateProductSell(Long id, ProductRequest productRequest);
    // elasticsearch
    Product updateProductShip(Long id, ProductRequest productRequest);
    // elasticsearch
    void updateIsPublic(Long id, boolean isPublic);
    // // elasticsearch
    public void deleteSizeQuantityById(Long id);

    public void incrementProductView(Long id);
    List<Product> getListByShopId(Long shopId);
    public SizeQuantity findByIdSizeQuantity(Long id);
    Product getById(Long id);

    Page<Product> findAllWithFiltersAndSorting(String deeplearning,String name,
                                               Long idcategory,
                                               Double price_min,
                                               Double price_max,
                                               String sortBy,
                                               String order,
                                               Integer rating_filter,
                                               Pageable pageable);

    Page<Product> findAllWithFiltersAndSortingE(String deeplearning,String name,
    Long idcategory,
    Double price_min,
    Double price_max,
    String sortBy,
    String order,
    Integer rating_filter,
    Pageable pageable);
    Product findProductWithSize(Long idProduct, Long idSizeQuantity);
    Product_Promotion_SizeQuantityy_GET Product_Promotion_SizeQuantityy_GET_(Long idproduct, Long idSizeQuantity, Long idPromotion, int quantity);
    List<Product_Promotion_SizeQuantityy_GET> getOderDetails(ReponseOrderData reponseOrderData);
    void plusView(Long id);
    List<OrderData> listOrderData(List<OrderDataRequest> orderDataRequests);
    public List<Product> getProductsByIds(List<Long> ids);
    
    // elasticsearch
    String HandleQuantityProductAdd(String o);
    // elasticsearch
    String HandleQuantityProductSub(String o);
}
