package com.example.productservice.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String code;
    private String status;
    private String description;
    private double discountAmount;
    private Long idShop;

    @ManyToMany(mappedBy = "promotions")
    private List<Product> products;

}
