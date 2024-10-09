package com.example.userservice.service;

import com.example.userservice.Entity.Shop;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ShopRedis {
    void setShop(Long id, Shop shop) throws JsonProcessingException;
    Shop getShop(Long id);
    void deleteShop(Long id);
}
