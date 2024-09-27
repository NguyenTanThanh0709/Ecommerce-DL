package com.example.userservice.User;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse {
    private String message;
    private AuthResponse data;
}
