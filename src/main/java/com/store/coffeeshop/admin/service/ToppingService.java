package com.store.coffeeshop.admin.service;

import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface ToppingService {

    public List<Topping> getAllToppings(Pageable pageable);

    public Topping getToppingById(Long id);

    public Topping createTopping(@Valid Topping topping);

    public Topping updateTopping(@Valid Topping topping);

    public void deleteTopping(Long id);

    List<Topping> findMostUsedToppingForDrink(Long drinkId);
}
