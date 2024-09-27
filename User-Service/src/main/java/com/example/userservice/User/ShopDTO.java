package com.example.userservice.User;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDTO
{
    private Long id;

    private String name;
    private String description;
    private String type;

    private String city;
    private String district;
    private String ward;
    private String detailLocation;
    private Long seller;
}
