package com.example.userservice.service.Impl;

import com.example.userservice.Entity.User;
import com.example.userservice.auth.UserDTO;
import com.example.userservice.repositoty.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.service.impRedis.UserRedisImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private UserRedisImpl userRedis;

    @Override
    public boolean checkUserExist(String email, String phone) {
        if(userRepository.existsByEmail(email) || userRepository.existsByPhone(phone)){
            return true;
        }
        return  false;
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

//            map ở đây
//            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
//            user.setPassword(userDTO.getPassword());
            user.setCity(userDTO.getCity());
            user.setDistrict(userDTO.getDistrict());
            user.setWard(userDTO.getWard());
            user.setDetailLocation(userDTO.getDetailLocation());

            User user_ = userRepository.save(user); // Lưu người dùng đã cập nhật vào cơ sở dữ liệu
            userRedis.deleteUser(user_.getId());
            return user_;
        } else {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) throws JsonProcessingException {
        User user_ =  userRedis.getUser(id);
        if(user_ != null){
            return user_;
        }else{
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                User user2 = user.get();
                userRedis.setUser(id, user2);
                return user2;
            }
            return null;
        }
    }

    @Override
    public boolean updatePassword(Long id, String passOld, String passNew) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User userEntity = user.get();
            if(passwordEncoder.matches(passOld, userEntity.getPassword())) {
                userRepository.updatePasswordById(id, passwordEncoder.encode(passNew));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}



