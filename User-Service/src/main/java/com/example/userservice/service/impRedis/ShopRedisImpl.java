package com.example.userservice.service.impRedis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.userservice.Entity.Shop;
import com.example.userservice.Entity.User;
import com.example.userservice.service.ShopRedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopRedisImpl implements ShopRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    // private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;    
    private final String SHOP_KEY = "shop_";

    @Override
    public void setShop(Long id, Shop shop) throws JsonProcessingException {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setShop'");
        var shopKey = SHOP_KEY + id;
        String json = objectMapper.writeValueAsString(shop);
        redisTemplate.opsForValue().set(shopKey, json);
        log.info("EmployeeServiceImpl.updateEmployee() : cache delete >> " + shop.toString());
    }

    @Override
    public Shop getShop(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getShop'");
        Shop shop = null;
        var shopKey = SHOP_KEY + id;
        String json = (String) redisTemplate.opsForValue().get(shopKey);
        if(json != null){
            try {
                shop = objectMapper.readValue(json, Shop.class);
                log.info("EmployeeServiceImpl.findEmployeeById() : cache insert >> " + shop.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return shop;
    }

    @Override
    public void deleteShop(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteShop'");
        var shopKey = SHOP_KEY + id;
        boolean hasKey = redisTemplate.hasKey(shopKey);
        if(hasKey){
            redisTemplate.delete(shopKey);
            log.info("EmployeeServiceImpl.updateEmployee() : cache delete >> " + shopKey.toString());
        }
    }
    
}
