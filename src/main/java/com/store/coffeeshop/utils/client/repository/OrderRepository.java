package com.store.coffeeshop.utils.client.repository;

import com.store.coffeeshop.utils.client.repository.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
