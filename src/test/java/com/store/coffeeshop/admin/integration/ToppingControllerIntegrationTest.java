package com.store.coffeeshop.admin.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.admin.service.impl.ToppingServiceImpl;
import com.store.coffeeshop.client.model.CartItem;
import com.store.coffeeshop.client.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToppingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToppingRepository toppingRepository;

    @MockBean
    private DrinkRepository drinkRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @Autowired
    private ToppingServiceImpl toppingServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private Topping topping1;
    private Topping topping2;
    private Drink drink;

    @BeforeEach
    public void setup() {
        topping1 = new Topping(1L, "Milk", 2.0);
        topping2 = new Topping(2L, "Lemon", 3.0);
        drink = new Drink(1L, "Black Coffee", 5.0);
    }

    @Test
    public void testGetAllToppings() throws Exception {
        List<Topping> toppings = Arrays.asList(topping1, topping2);
        Page<Topping> page = new PageImpl<>(toppings);

        Mockito.when(toppingRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/toppings"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Milk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Lemon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(3.0));

        Mockito.verify(toppingRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    public void testGetToppingByToppingId() throws Exception {
        Mockito.when(toppingRepository.findById(1L)).thenReturn(Optional.ofNullable(topping1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/toppings/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Milk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(2.0));

        Mockito.verify(toppingRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testCreateTopping() throws Exception {
        Topping newTopping = new Topping(1L,"Sugar", 1.5);
        Mockito.when(toppingRepository.save(Mockito.any(Topping.class))).thenReturn(newTopping);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/toppings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTopping)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Sugar"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1.5));

        Mockito.verify(toppingRepository, Mockito.times(1)).save(Mockito.any(Topping.class));
    }

    @Test
    public void testUpdateTopping() throws Exception {
        Topping updatedTopping = new Topping(2L, "Lemon", 1.0);
        Mockito.when(toppingRepository.findById(2L)).thenReturn(Optional.ofNullable(topping2));
        Mockito.when(toppingRepository.save(Mockito.any(Topping.class))).thenReturn(updatedTopping);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/toppings", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTopping)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lemon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1.0));

        Mockito.verify(toppingRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(toppingRepository, Mockito.times(1)).save(Mockito.any(Topping.class));
    }

    @Test
    public void testDeleteTopping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/toppings/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(toppingRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testFindMostUsedToppingForDrink() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/toppings/most-used/{drinkId}", drinkId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(topping1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(topping2.getId()));

        Mockito.verify(drinkRepository, Mockito.times(1)).findById(drinkId);
        Mockito.verify(cartItemRepository, Mockito.times(1)).findByDrink(drink);
        Mockito.verify(toppingRepository, Mockito.times(1)).findAllById(Arrays.asList(1L, 2L, 3L));
    }
}
