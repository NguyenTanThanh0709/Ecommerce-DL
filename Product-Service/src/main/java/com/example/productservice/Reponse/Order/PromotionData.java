package com.example.productservice.Reponse.Order;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PromotionData {
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
