package com.example.cartservice.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse {
    private String message;
    private List<CartData> data;

}
