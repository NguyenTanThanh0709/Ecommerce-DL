package com.example.userservice.service;

import com.example.userservice.Entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserRedis {
    void setUser(Long id, User user) throws JsonProcessingException;
    User getUser(Long id);
    void deleteUser(Long id);
}
