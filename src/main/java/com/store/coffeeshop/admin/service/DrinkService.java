package com.store.coffeeshop.admin.service;

import com.store.coffeeshop.admin.model.Drink;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface DrinkService {

    public List<Drink> getAllDrinks(Pageable pageable);

    public Drink getDrinkById(Long id);

    public Drink createDrink(@Valid Drink drink);

    public Drink updateDrink(@Valid Drink drink);

    public void deleteDrink(Long id);
}
