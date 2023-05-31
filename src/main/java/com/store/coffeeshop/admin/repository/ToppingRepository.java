package com.store.coffeeshop.admin.repository;

import com.store.coffeeshop.admin.model.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {

    Optional<Topping> getToppingByName(String name);
}
