package com.example.userservice.User;

import com.example.userservice.auth.UserDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String access_token;
    private Long expires;
    private String refresh_token;
    private Long expires_refresh_token;
    private UserDTO user;

}
