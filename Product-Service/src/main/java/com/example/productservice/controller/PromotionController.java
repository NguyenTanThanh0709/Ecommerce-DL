package com.example.productservice.controller;

import com.example.productservice.Entity.Promotion;
import com.example.productservice.Reponse.PromotionRequest;
import com.example.productservice.Service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/")
    public ResponseEntity<?> createPromotion(@RequestBody PromotionRequest promotionRequest) {
        Promotion promotion=  promotionService.savePromotion(promotionRequest);
        if(promotion!= null){
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("NOT OK", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody PromotionRequest promotionRequest) {
        Promotion promotion = promotionService.updatePromotion(id, promotionRequest);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    @GetMapping("/shop/{idShop}")
    public ResponseEntity<List<Promotion>> getPromotionsByIdShop(@PathVariable Long idShop) {
        List<Promotion> promotions = promotionService.getPromotionsByIdShop(idShop);
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionsById(@PathVariable Long id) {
        Promotion promotions = promotionService.getById(id);
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/product/{productId}/active")
    public ResponseEntity<List<Promotion>> getActivePromotionsByProductId(@PathVariable Long productId) {
        List<Promotion> promotions = promotionService.getActivePromotionsByProductId(productId);
        return ResponseEntity.ok(promotions);
    }
}
