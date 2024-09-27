package com.example.userservice.auth;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;

    private String phone;
    private String city;
    private String district;
    private String ward;
    private String detailLocation;



}