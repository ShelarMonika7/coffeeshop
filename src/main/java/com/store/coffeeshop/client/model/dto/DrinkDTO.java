package com.store.coffeeshop.client.model.dto;

import com.store.coffeeshop.admin.model.Topping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrinkDTO {

    private Long id;
    private String name;
    private double amount;
    private List<Topping> toppings;
    private double totalAmount;

}
