package com.store.coffeeshop.admin.repository;

import com.store.coffeeshop.admin.model.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {
   Optional<Drink> getDrinkByName(String name);
}
