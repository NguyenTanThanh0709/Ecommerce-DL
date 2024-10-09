package com.example.userservice.service.Impl;

import com.example.userservice.Entity.Shop;
import com.example.userservice.Entity.User;
import com.example.userservice.User.ShopDTO;
import com.example.userservice.repositoty.ShopRepository;
import com.example.userservice.repositoty.UserRepository;
import com.example.userservice.service.ShopService;
import com.example.userservice.service.impRedis.ShopRedisImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ShopImpl implements ShopService {
    @Autowired
    private  ShopRepository shopRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  ShopRedisImpl shopRedis;


    @Override
    public Shop registerShop(ShopDTO shopDTO) {
        Shop shop = new Shop();

        shop.setName(shopDTO.getName());

        shop.setDescription(shopDTO.getDescription());
        shop.setType(shopDTO.getType());
        shop.setCity(shopDTO.getCity());
        shop.setDistrict(shopDTO.getDistrict());
        shop.setWard(shopDTO.getWard());
        shop.setDetailLocation(shopDTO.getDetailLocation());
        User user = userRepository.findById(shopDTO.getSeller())
                .orElseThrow(() -> new RuntimeException("User not found"));
        shop.setSeller(user);
        return shopRepository.save(shop);
    }

    @Override
    public List<Shop> getListShopByDistrict(String district) {
        return shopRepository.findByDistrictContainingIgnoreCase(district);
    }

    @Override
    public Shop updateShop(ShopDTO shopDTO) {
        Shop existingShop = shopRepository.findById(shopDTO.getId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        existingShop.setName(shopDTO.getName());
        existingShop.setDescription(shopDTO.getDescription());
        existingShop.setType(shopDTO.getType());
        existingShop.setCity(shopDTO.getCity());
        existingShop.setDistrict(shopDTO.getDistrict());
        existingShop.setWard(shopDTO.getWard());
        existingShop.setDetailLocation(shopDTO.getDetailLocation());
        Shop updatedShop = shopRepository.save(existingShop);
        shopRedis.deleteShop(existingShop.getId());
        return updatedShop;
    }

    @Override
    public Shop getShopById(Long id) {
        return shopRepository.findById(id).orElse(null);
    }

    @Override
    public Shop getShopBySellerId(Long sellerId) throws JsonProcessingException {
        Shop shop = shopRedis.getShop(sellerId);
        if(shop != null){
            return shop;
        }
        shop = shopRepository.findBySellerId(sellerId).orElse(null);
        if(shop != null){
            shopRedis.setShop(sellerId, shop);
        }
        return shop;
    }

    @Override
    public Long doesShopExistForSeller(Long sellerId) throws JsonProcessingException {
        if(shopRepository.existsBySellerId(sellerId)){
            Shop shop = this.getShopBySellerId(sellerId);
            return shop.getId();
        }else {
            return 0l;
        }
    }
}
