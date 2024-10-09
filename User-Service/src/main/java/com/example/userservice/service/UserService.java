package com.example.userservice.service;

import com.example.userservice.Entity.User;
import com.example.userservice.auth.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {
    boolean checkUserExist(String email, String phone);
    // change redis
    User updateUser(UserDTO userDTO);
    // change redis
    boolean updatePassword(Long id,String passOld, String passNew);
    User getUserById(Long id) throws JsonProcessingException;
}
