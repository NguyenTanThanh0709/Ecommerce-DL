package com.example.productservice.Service;

import com.example.productservice.Entity.Promotion;
import com.example.productservice.Reponse.PromotionRequest;

import java.util.List;

public interface PromotionService {
    public Promotion savePromotion(PromotionRequest promotionRequest);
    public Promotion updatePromotion(Long id, PromotionRequest promotionRequest);
    public List<Promotion> getPromotionsByIdShop(Long idShop);
    public  Promotion getById(Long id);
    public List<Promotion> getActivePromotionsByProductId(Long productId);
}
