package com.example.userservice.service.Impl;

import com.example.userservice.Entity.User;
import com.example.userservice.auth.UserDTO;
import com.example.userservice.repositoty.UserRepository;
import com.example.userservice.service.UserService;
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

            userRepository.save(user); // Lưu người dùng đã cập nhật vào cơ sở dữ liệu
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
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



