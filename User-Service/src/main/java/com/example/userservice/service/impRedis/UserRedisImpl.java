package com.example.userservice.service.impRedis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.userservice.Entity.User;
import com.example.userservice.service.UserRedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserRedisImpl implements UserRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    // private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;    
    private final String USER_KEY = "user_";

    @Override
    public void setUser(Long id, User user) throws JsonProcessingException {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'setUser'");
        var userKey = USER_KEY + id;
        String json = objectMapper.writeValueAsString(user);
        redisTemplate.opsForValue().set(userKey, json);
        log.info("EmployeeServiceImpl.updateEmployee() : cache delete >> " + user.toString());
    }

    @Override
    public User getUser(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getUser'");
        User user = null;
        var userKey = USER_KEY + id;
        String json = (String) redisTemplate.opsForValue().get(userKey);
        if(json != null){
            try {
                user = objectMapper.readValue(json, User.class);
                log.info("EmployeeServiceImpl.findEmployeeById() : cache insert >> " + user.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
        var userKey = USER_KEY + id;
        boolean hasKey = redisTemplate.hasKey(userKey);
        if(hasKey){
            redisTemplate.delete(userKey);
            log.info("EmployeeServiceImpl.updateEmployee() : cache delete >> " + userKey.toString());
        }
    }
    
}
