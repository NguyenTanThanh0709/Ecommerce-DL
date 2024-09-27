package com.example.userservice.service;

import com.example.userservice.Entity.Shop;
import com.example.userservice.User.ShopDTO;

import java.util.List;

public interface ShopService {
    Shop registerShop(ShopDTO shopDTO);
    Shop updateShop(ShopDTO shopDTO);
    Shop getShopById(Long id);
    Shop getShopBySellerId(Long sellerId);
    Long doesShopExistForSeller(Long sellerId);

    List<Shop> getListShopByDistrict(String district);
}
