package com.example.userservice.User;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePassword {
    private String confirm_password;
    private String new_password;
    private String password;
}