package com.store.coffeeshop.client.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
        private double cartAmount;
        private double discountedAmount;
        private List<DrinkDTO> drinks;
}
