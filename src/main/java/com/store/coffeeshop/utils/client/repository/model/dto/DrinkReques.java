package com.store.coffeeshop.utils.client.repository.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkReques {
    private String name;
    private List<String> toppings;
}
