package com.example.productservice.Reponse;

import com.example.productservice.Product.Order_Cart.ProductReponseCart_Order;
import com.example.productservice.Product.Order_Cart.SizeQuantityReponseCart_Order;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_Promotion_SizeQuantityy_GET {
    private ProductReponseCart_Order product;
    private SizeQuantityReponseCart_Order sizeQuantity;
    private PromotionRequest promotionRequest;
    private  int quantity;
}
