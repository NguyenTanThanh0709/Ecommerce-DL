package com.example.purchaseservice.Request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long id;
    private Long customerId;
    private Long shopId;
    private double totalMoney;
    private String status; // Trạng thái hiện tại của đơn hàng (ví dụ: pending, paid, shipped, delivered, cancelled)

    private List<OrderItemRequest> orderItems;
}
