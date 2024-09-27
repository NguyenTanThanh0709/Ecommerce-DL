package com.example.cartservice.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    @NotNull(message = "Please input Product Id")
    private Long idProduct;
    private Long idSizeQuantity;
    @NotNull(message = "Please input Quantity Id")
    @Min(value = -1, message = "Minimum quantity is 1")
    private int quantity;

}