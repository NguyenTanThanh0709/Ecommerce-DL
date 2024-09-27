package com.example.userservice.auth;



import com.example.userservice.User.AuthResponse;
import com.example.userservice.User.SuccessResponse;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @Autowired
    private UserService iUser;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO request) {
        if (iUser.checkUserExist(request.getEmail(), request.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng với thông tin này đã tồn tại");
        }
        AuthenticationResponse data = service.register(request);
        UserDTO userDTO = new UserDTO(data.getId(), data.getEmail(), data.getPhone(), "password", data.getCity(), data.getDistrict(), data.getWard(), data.getDetailLocation());
        AuthResponse authResponse = new AuthResponse(data.getAccessToken(), 604800l, data.getRefreshToken(), 8640000l,userDTO );
        SuccessResponse SuccessResponse = new SuccessResponse("Đăng kí thành công", authResponse);
        return ResponseEntity.ok(SuccessResponse);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginDTO request
    ) {
        AuthenticationResponse data = service.authenticate(request);
        UserDTO userDTO = new UserDTO(data.getId(), data.getEmail(), data.getPhone(), "password", data.getCity(), data.getDistrict(), data.getWard(), data.getDetailLocation());
        AuthResponse authResponse = new AuthResponse(data.getAccessToken(), 604800l, data.getRefreshToken(), 8640000l,userDTO );
        SuccessResponse SuccessResponse = new SuccessResponse("Đăng kí thành công", authResponse);
        return ResponseEntity.ok(SuccessResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public Message logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Message message = new Message("Đăng xuất thành công");
        return message;
    }
}