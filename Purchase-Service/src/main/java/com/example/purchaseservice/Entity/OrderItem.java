package com.example.purchaseservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Long idSizeQuantity; // cho phép null nếu là các sản phẩm k có size
    private Long promotionId; // cho phép null nếu là các sản phẩm k có promotion
    private int quantity; // số lượng sản phẩm
    private String note;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

}
