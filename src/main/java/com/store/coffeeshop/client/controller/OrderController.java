package com.store.coffeeshop.client.controller;

import com.store.coffeeshop.admin.service.DrinkService;
import com.store.coffeeshop.client.model.dto.OrderRequest;
import com.store.coffeeshop.client.model.dto.OrderResponse;
import com.store.coffeeshop.client.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final DrinkService drinkService;

    @Autowired
    public OrderController(OrderService orderService, DrinkService drinkService) {
        this.orderService = orderService;
        this.drinkService = drinkService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest).getBody());
    }
}

