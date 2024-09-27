package com.example.productservice.Product;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionReponse {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String code;
    private String status;
    private String description;
    private double discountAmount;
    private Long idShop;
}
