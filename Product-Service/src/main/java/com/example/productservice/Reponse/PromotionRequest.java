package com.example.productservice.Reponse;

import jakarta.persistence.ElementCollection;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRequest {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String code;
    private String status;
    private String description;
    private double discountAmount;
    private Long idShop;

    @ElementCollection
    private List<Long> idProducts;
}
