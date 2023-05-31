package com.store.coffeeshop.client.unit;

import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.admin.service.impl.DrinkServiceImpl;
import com.store.coffeeshop.client.model.Cart;
import com.store.coffeeshop.client.model.CartItem;
import com.store.coffeeshop.client.model.Order;
import com.store.coffeeshop.client.model.dto.DrinkDTO;
import com.store.coffeeshop.client.model.dto.DrinkReques;
import com.store.coffeeshop.client.model.dto.OrderRequest;
import com.store.coffeeshop.client.model.dto.OrderResponse;
import com.store.coffeeshop.client.repository.CartItemRepository;
import com.store.coffeeshop.client.repository.CartRepository;
import com.store.coffeeshop.client.repository.OrderRepository;
import com.store.coffeeshop.client.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderControllerTest {

        @MockBean
        ToppingRepository toppingRepository;

        @MockBean
        DrinkRepository drinkRepository;

        @MockBean
        CartRepository cartRepository;

        @MockBean
        OrderRepository orderRepository;

        @MockBean
        CartItemRepository cartItemRepository;

        @Autowired
        DrinkServiceImpl drinkServiceImpl;

        @Autowired
        OrderServiceImpl orderServiceImpl;


        @Test
        public void testCreateOrder() throws Exception {

                Drink drink1 = new Drink(1L, "Black Coffee", 4.0);
                Drink drink2 = new Drink(2L, "Latte", 5.0);

                Topping topping1 = new Topping(1L, "Milk", 2.0);
                Topping topping2 = new Topping(2L, "Lemon", 3.0);

                // Mocking repository methods
                Mockito.when(drinkRepository.getDrinkByName(drink1.getName())).thenReturn(Optional.of(drink1));
                Mockito.when(drinkRepository.getDrinkByName(drink2.getName())).thenReturn(Optional.of(drink2));
                Mockito.when(toppingRepository.getToppingByName(topping1.getName())).thenReturn(Optional.of(topping1));
                Mockito.when(toppingRepository.getToppingByName(topping2.getName())).thenReturn(Optional.of(topping2));

                // Request data
                OrderRequest orderRequest = new OrderRequest(Arrays.asList(
                        new DrinkReques(drink1.getName(), Arrays.asList(topping1.getName(), topping2.getName())),
                        new DrinkReques(drink2.getName(), Arrays.asList(topping1.getName()))
                ));

                // Expected response data
                double cartAmount = 16.0;
                double discountedAmount = 12.0;
                List<DrinkDTO> drinks = Arrays.asList(
                        new DrinkDTO(1L, drink1.getName(), drink1.getAmount(), Arrays.asList(topping1, topping2), 9.0),
                        new DrinkDTO(2L, drink2.getName(), drink2.getAmount(), Arrays.asList(topping1), 7.0)
                );

                Cart cart1 = new Cart(1L,null,0.0,0.0);
                CartItem cartItem1 = new CartItem(1L, drink1, Arrays.asList(topping1, topping2), cart1);
                CartItem cartItem2 = new CartItem(2L, drink2, Arrays.asList(topping1), cart1);
                List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

                Cart cart = new Cart(1L, cartItems, cartAmount, discountedAmount);
                Order order = new Order(1L, cart);

                // Method under test
                ResponseEntity<OrderResponse> responseEntity = orderServiceImpl.createOrder(orderRequest);

                // Assertions
                Assertions.assertAll(
                        () -> Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                        () -> Assertions.assertNotNull(responseEntity.getBody()),
                        () -> Assertions.assertEquals(cartAmount, responseEntity.getBody().getCartAmount()),
                        () -> Assertions.assertEquals(discountedAmount, responseEntity.getBody().getDiscountedAmount()),
                        () -> Assertions.assertEquals(drinks, responseEntity.getBody().getDrinks())
                );

        }

}



