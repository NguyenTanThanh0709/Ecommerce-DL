package com.example.productservice.Service.impl;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.Promotion;
import com.example.productservice.Reponse.PromotionRequest;
import com.example.productservice.Repository.ProductRepository;
import com.example.productservice.Repository.PromotionRepository;
import com.example.productservice.Service.PromotionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Transactional
    @Override
    public Promotion savePromotion(PromotionRequest promotionRequest) {
        List<Product> products = productRepository.findAllById(promotionRequest.getIdProducts());

        Promotion promotion = Promotion.builder()
                .name(promotionRequest.getName())
                .startDate(promotionRequest.getStartDate())
                .endDate(promotionRequest.getEndDate())
                .code(promotionRequest.getCode())
                .status(promotionRequest.getStatus())
                .description(promotionRequest.getDescription())
                .discountAmount(promotionRequest.getDiscountAmount())
                .idShop(promotionRequest.getIdShop())
                .products(products)
                .build();

        Promotion savedPromotion = promotionRepository.save(promotion);

        // Update products with the new promotion
        products.forEach(product -> product.getPromotions().add(savedPromotion));
        productRepository.saveAll(products);

        return savedPromotion;
    }

    @Transactional
    @Override
    public Promotion updatePromotion(Long id, PromotionRequest promotionRequest) {
        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);

        if (optionalPromotion.isEmpty()) {
            throw new IllegalArgumentException("Promotion not found");
        }

        Promotion promotion = optionalPromotion.get();
        promotion.setName(promotionRequest.getName());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setCode(promotionRequest.getCode());
        promotion.setStatus(promotionRequest.getStatus());
        promotion.setDescription(promotionRequest.getDescription());
        promotion.setDiscountAmount(promotionRequest.getDiscountAmount());
        promotion.setIdShop(promotionRequest.getIdShop());

        // Update the list of products
        List<Product> newProducts = productRepository.findAllById(promotionRequest.getIdProducts());
        List<Product> oldProducts = promotion.getProducts();

        // Remove the promotion from old products
        oldProducts.forEach(product -> product.getPromotions().remove(promotion));

        // Add the promotion to new products
        newProducts.forEach(product -> product.getPromotions().add(promotion));

        promotion.setProducts(newProducts);

        Promotion updatedPromotion = promotionRepository.save(promotion);

        productRepository.saveAll(oldProducts);
        productRepository.saveAll(newProducts);

        return updatedPromotion;
    }

    @Override
    public List<Promotion> getPromotionsByIdShop(Long idShop) {
        return promotionRepository.findByIdShop(idShop);
    }

    @Override
    public Promotion getById(Long id) {
        return promotionRepository.findById(id).get();
    }

    @Override
    public List<Promotion> getActivePromotionsByProductId(Long productId) {
        return promotionRepository.findActivePromotionsByProductId(productId);
    }
}
