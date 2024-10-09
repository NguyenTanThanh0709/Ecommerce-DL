package com.example.userservice.service;

import com.example.userservice.Entity.Shop;
import com.example.userservice.User.ShopDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ShopService {
    Shop registerShop(ShopDTO shopDTO);
    // change redis
    Shop updateShop(ShopDTO shopDTO);
    Shop getShopById(Long id);
    Shop getShopBySellerId(Long sellerId) throws JsonProcessingException;
    Long doesShopExistForSeller(Long sellerId) throws JsonProcessingException;

    // add redis
    List<Shop> getListShopByDistrict(String district);
}
