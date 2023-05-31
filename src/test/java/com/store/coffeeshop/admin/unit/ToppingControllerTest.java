package com.store.coffeeshop.admin.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.admin.service.impl.ToppingServiceImpl;
import com.store.coffeeshop.client.model.CartItem;
import com.store.coffeeshop.client.repository.CartItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ToppingControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @MockBean
    ToppingRepository toppingRepository;

    @MockBean
    DrinkRepository drinkRepository;

    @Autowired
    ToppingServiceImpl toppingServiceImpl;

    @MockBean
    CartItemRepository cartItemRepository;

    @Mock
    private Pageable pageable;


    Topping topping1 = new Topping(1L,"Milk",2.0);
    Topping topping2 = new Topping(2L,"Lemon",3.0);

    Drink drink = new Drink(1L,"Black Coffee",5);

    @Test
    public void testGetAllToppings() throws Exception {

        List<Topping> toppings = Arrays.asList(topping1, topping2);
        Page<Topping> page = new PageImpl<Topping>(toppings);

        Mockito.when(toppingRepository.findAll(pageable)).thenReturn(page);
        Assertions.assertEquals(2,toppingServiceImpl.getAllToppings(pageable).size());

    }

    @Test
    public void testGetToppingByToppingId() throws Exception {

        Mockito.when(toppingRepository.findById(topping1.getId())).thenReturn(Optional.ofNullable(topping1));
        Assertions.assertEquals(topping1,toppingServiceImpl.getToppingById(topping1.getId()));
    }

    @Test
    public void testCreateTopping() throws Exception {

        Mockito.when(toppingRepository.save(topping1)).thenReturn(topping1);
        Assertions.assertEquals(topping1,toppingServiceImpl.createTopping(topping1));

    }

    @Test
    public void testUpdateTopping() throws Exception {
        Topping updatedTopping = new Topping(2L,"Lemon",1.0);

        Mockito.when(toppingRepository.findById(updatedTopping.getId())).thenReturn(Optional.ofNullable(topping2));
        topping2.setName(updatedTopping.getName());
        topping2.setAmount(updatedTopping.getAmount());

        Mockito.when(toppingRepository.save(topping2)).thenReturn(topping2);

        Assertions.assertEquals(topping2,toppingServiceImpl.updateTopping(topping2));

    }

    @Test
    public void testDeleteTopping() throws Exception {

        toppingRepository.delete(topping1);
        Mockito.verify(toppingRepository,Mockito.times(1)).delete(topping1);

    }

    @Test
    public void testFindMostUsedToppingForDrink() {
        Long drinkId = 1L;

        // Mock the Drink entity and repository
        Drink drink = new Drink();
        drink.setId(drinkId);
        Mockito.when(drinkRepository.findById(drinkId)).thenReturn(Optional.of(drink));

        // Mock the CartItem entities and repository
        CartItem cartItem1 = new CartItem();
        cartItem1.setToppings(Arrays.asList(new Topping(1L, "Milk", 2.0), new Topping(2L, "Lemon", 3.0)));
        CartItem cartItem2 = new CartItem();
        cartItem2.setToppings(Arrays.asList(new Topping(1L, "Milk", 2.0), new Topping(3L, "Sugar", 1.5)));
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
        Mockito.when(cartItemRepository.findByDrink(drink)).thenReturn(cartItems);

        // Mock the Topping entities and repository
        Topping topping1 = new Topping(1L, "Milk", 2.0);
        Topping topping2 = new Topping(2L, "Lemon", 3.0);
        Topping topping3 = new Topping(3L, "Sugar", 1.5);
        Mockito.when(toppingRepository.findAllById(Arrays.asList(1L, 2L, 3L))).thenReturn(Arrays.asList(topping1, topping2, topping3));

        // Invoke the method under test
        List<Topping> mostUsedToppings = toppingServiceImpl.findMostUsedToppingForDrink(drinkId);

        // Verify the expected behavior
        Assertions.assertEquals(3, mostUsedToppings.size());
        Assertions.assertEquals(topping1, mostUsedToppings.get(0));
        Assertions.assertEquals(topping2, mostUsedToppings.get(1));

        // Verify the interactions with the repositories
        Mockito.verify(drinkRepository, Mockito.times(1)).findById(drinkId);
        Mockito.verify(cartItemRepository, Mockito.times(1)).findByDrink(drink);
        Mockito.verify(toppingRepository, Mockito.times(1)).findAllById(Arrays.asList(1L, 2L, 3L));
    }


}
