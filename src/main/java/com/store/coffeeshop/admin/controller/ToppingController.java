package com.store.coffeeshop.admin.controller;

import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.service.ToppingService;
import com.store.coffeeshop.admin.model.Topping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/toppings")
public class ToppingController {

    private final ToppingService toppingService;

    @Autowired
    public ToppingController(ToppingService toppingService) {
        this.toppingService = toppingService;
    }

    @GetMapping
    public List<Topping> getAllToppings(Pageable pageable) {
        return toppingService.getAllToppings(pageable);
    }

    @GetMapping("/{id}")
    public Topping getToppingById(@PathVariable Long id) {
        return toppingService.getToppingById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topping createTopping(@Valid @RequestBody Topping topping) {
        return toppingService.createTopping(topping);
    }

    @PutMapping
    public Topping updateTopping(@Valid @RequestBody Topping topping) {
        return toppingService.updateTopping(topping);
    }

    @DeleteMapping("/{id}")
    public void deleteTopping(@PathVariable Long id) {
        toppingService.deleteTopping(id);
    }

    @GetMapping("/most-used/{drinkId}")
    public List<Topping> findMostUsedToppingForDrink(@PathVariable Long drinkId) {
        return toppingService.findMostUsedToppingForDrink(drinkId);
    }
}
