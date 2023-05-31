package com.store.coffeeshop.utils.client.repository;

import com.store.coffeeshop.utils.client.repository.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
