package com.example.productservice.Reponse.ReponseOrder;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReponseOrderData {
    private List<ReponseOrder> orderItems;
}
