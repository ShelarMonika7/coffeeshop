package com.store.coffeeshop.client.service;

import com.store.coffeeshop.client.model.dto.OrderRequest;
import com.store.coffeeshop.client.model.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest);

}
