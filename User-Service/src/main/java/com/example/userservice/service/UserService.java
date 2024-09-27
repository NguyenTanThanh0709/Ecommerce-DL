package com.example.userservice.service;

import com.example.userservice.Entity.User;
import com.example.userservice.auth.UserDTO;

public interface UserService {
    boolean checkUserExist(String email, String phone);
    User updateUser(UserDTO userDTO);
    User getUserById(Long id);
    boolean updatePassword(Long id,String passOld, String passNew);
}
