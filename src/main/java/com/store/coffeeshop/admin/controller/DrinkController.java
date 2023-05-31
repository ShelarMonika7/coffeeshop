package com.store.coffeeshop.admin.controller;


import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping
    public List<Drink> getAllDrinks(Pageable pageable) {
        return drinkService.getAllDrinks(pageable);
    }

    @GetMapping("/{id}")
    public Drink getDrinkById(@PathVariable Long id) {
        return drinkService.getDrinkById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Drink createDrink(@Valid @RequestBody Drink drink) {
        return drinkService.createDrink(drink);
    }

    @PutMapping
    public Drink updateDrink(@Valid @RequestBody Drink drink) {
        return drinkService.updateDrink(drink);
    }

    @DeleteMapping("/{id}")
    public void deleteDrink(@PathVariable Long id) {
        drinkService.deleteDrink(id);
    }
}
