package com.store.coffeeshop.client.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.admin.service.impl.DrinkServiceImpl;
import com.store.coffeeshop.client.model.dto.DrinkReques;
import com.store.coffeeshop.client.model.dto.OrderRequest;
import com.store.coffeeshop.client.repository.CartItemRepository;
import com.store.coffeeshop.client.repository.CartRepository;
import com.store.coffeeshop.client.repository.OrderRepository;
import com.store.coffeeshop.client.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToppingRepository toppingRepository;

    @MockBean
    private DrinkRepository drinkRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @Autowired
    private DrinkServiceImpl drinkServiceImpl;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOrder() throws Exception {
        // Define your test data
        Drink drink1 = new Drink(1L, "Black Coffee", 4.0);
        Drink drink2 = new Drink(2L, "Latte", 5.0);

        Topping topping1 = new Topping(1L, "Milk", 2.0);
        Topping topping2 = new Topping(2L, "Lemon", 3.0);

        // Mock the repository methods
        Mockito.when(drinkRepository.getDrinkByName(drink1.getName())).thenReturn(Optional.of(drink1));
        Mockito.when(drinkRepository.getDrinkByName(drink2.getName())).thenReturn(Optional.of(drink2));
        Mockito.when(toppingRepository.getToppingByName(topping1.getName())).thenReturn(Optional.of(topping1));
        Mockito.when(toppingRepository.getToppingByName(topping2.getName())).thenReturn(Optional.of(topping2));

        // Create your request object
        OrderRequest orderRequest = new OrderRequest(Arrays.asList(
                new DrinkReques(drink1.getName(), Arrays.asList(topping1.getName(), topping2.getName())),
                new DrinkReques(drink2.getName(), Arrays.asList(topping1.getName()))
        ));

        // Perform the HTTP request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartAmount").value(16.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discountedAmount").value(12.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].name").value(drink1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].amount").value(drink1.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].toppings.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].toppings[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].toppings[0].name").value(topping1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[0].toppings[0].amount").value(topping1.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].name").value(drink2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].amount").value(drink2.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].toppings.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].toppings[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].toppings[0].name").value(topping1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.drinks[1].toppings[0].amount").value(topping1.getAmount()));
    }

}
