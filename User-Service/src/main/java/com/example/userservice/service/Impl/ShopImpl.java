package com.example.userservice.service.Impl;

import com.example.userservice.Entity.Shop;
import com.example.userservice.Entity.User;
import com.example.userservice.User.ShopDTO;
import com.example.userservice.repositoty.ShopRepository;
import com.example.userservice.repositoty.UserRepository;
import com.example.userservice.service.ShopService;
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
        return shopRepository.save(existingShop);
    }

    @Override
    public Shop getShopById(Long id) {
        return shopRepository.findById(id).orElse(null);
    }

    @Override
    public Shop getShopBySellerId(Long sellerId) {
        return shopRepository.findBySellerId(sellerId).orElse(null);
    }

    @Override
    public Long doesShopExistForSeller(Long sellerId) {
        if(shopRepository.existsBySellerId(sellerId)){
            Shop shop = this.getShopBySellerId(sellerId);
            return shop.getId();
        }else {
            return 0l;
        }
    }
}
