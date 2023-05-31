package com.store.coffeeshop.admin.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
public class DrinkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkRepository drinkRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private Pageable pageable;

    private Drink drink1;
    private Drink drink2;

    @BeforeEach
    public void setup() {
        drink1 = new Drink(1L, "Black Coffee", 5.0);
        drink2 = new Drink(2L, "Latte", 5.0);
    }

    @Test
    public void testGetAllDrinks() throws Exception {
        List<Drink> drinks = Arrays.asList(drink1, drink2);
        Page<Drink> page = new PageImpl<>(drinks);

        Mockito.when(drinkRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drinks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Black Coffee"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(5.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Latte"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].amount").value(5.0));

        Mockito.verify(drinkRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }


    @Test
    public void testGetDrinkByDrinkId() throws Exception {
        Mockito.when(drinkRepository.findById(1L)).thenReturn(Optional.ofNullable(drink1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/drinks/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Black Coffee"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(5.0));

        Mockito.verify(drinkRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testCreateDrink() throws Exception {
        Drink newDrink = new Drink(1L,"Espresso", 4.0);
        Mockito.when(drinkRepository.save(Mockito.any(Drink.class))).thenReturn(newDrink);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/drinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDrink)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Espresso"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(4.0));

        Mockito.verify(drinkRepository, Mockito.times(1)).save(Mockito.any(Drink.class));
    }

    @Test
    public void testUpdateDrink() throws Exception {
        Drink updatedDrink = new Drink(2L, "Latte", 7.0);
        Mockito.when(drinkRepository.findById(2L)).thenReturn(Optional.ofNullable(drink2));
        Mockito.when(drinkRepository.save(Mockito.any(Drink.class))).thenReturn(updatedDrink);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/drinks", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDrink)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Latte"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(7.0));

        Mockito.verify(drinkRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(drinkRepository, Mockito.times(1)).save(Mockito.any(Drink.class));
    }

    @Test
    public void testDeleteDrink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/drinks/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(drinkRepository, Mockito.times(1)).deleteById(1L);
    }


}
