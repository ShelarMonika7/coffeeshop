package com.store.coffeeshop.utils.client.service;

import com.store.coffeeshop.utils.client.repository.model.dto.OrderRequest;
import com.store.coffeeshop.utils.client.repository.model.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest);

}
