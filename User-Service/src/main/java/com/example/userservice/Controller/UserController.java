package com.example.userservice.Controller;

import com.example.userservice.Entity.User;
import com.example.userservice.User.ProfileReponse;
import com.example.userservice.User.UpdatePassword;
import com.example.userservice.auth.UserDTO;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        ProfileReponse profileReponse = new ProfileReponse("OK", user);

        if (user != null) {
            return new ResponseEntity<>(profileReponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User updatedUser = userService.updateUser(userDTO);
        ProfileReponse profileReponse = new ProfileReponse("OK", updatedUser);
        if (updatedUser != null) {
            return new ResponseEntity<>(profileReponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody UpdatePassword updatePassword) {
        boolean isUpdated = userService.updatePassword(id, updatePassword.getPassword(), updatePassword.getNew_password());
        if (isUpdated) {
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Password update failed", HttpStatus.BAD_REQUEST);
        }
    }
}
