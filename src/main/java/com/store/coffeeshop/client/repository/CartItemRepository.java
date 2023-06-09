package com.store.coffeeshop.client.repository;

import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.client.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByDrink(Drink drink);
}
