package com.example.purchaseservice.Request;

import com.example.purchaseservice.Entity.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private Long id;
    private Long orderId;
    private Date paymentDate;
    private String status;
    private String method; // COD, ONLINE
    private double amount;
}
