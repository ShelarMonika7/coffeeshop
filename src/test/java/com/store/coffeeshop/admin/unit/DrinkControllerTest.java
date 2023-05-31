package com.store.coffeeshop.admin.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.service.impl.DrinkServiceImpl;
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
public class DrinkControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @MockBean
    DrinkRepository drinkRepository;

    @Autowired
    DrinkServiceImpl drinkServiceImpl;

    @Mock
    private Pageable pageable;


    Drink drink1 = new Drink(1L,"Black Coffee",5.0);
    Drink drink2 = new Drink(2L,"Latte",5.0);

    @Test
    public void testGetAllDrinks() throws Exception {

        List<Drink> drinks = Arrays.asList(drink1, drink2);
        Page<Drink> page = new PageImpl<Drink>(drinks);

        Mockito.when(drinkRepository.findAll(pageable)).thenReturn(page);
        Assertions.assertEquals(2,drinkServiceImpl.getAllDrinks(pageable).size());

    }

    @Test
    public void testGetDrinkByDrinkId() throws Exception {

        Mockito.when(drinkRepository.findById(drink1.getId())).thenReturn(Optional.ofNullable(drink1));
        Assertions.assertEquals(drink1,drinkServiceImpl.getDrinkById(drink1.getId()));
    }

    @Test
    public void testCreateTopping() throws Exception {

        Mockito.when(drinkRepository.save(drink1)).thenReturn(drink1);
        Assertions.assertEquals(drink1,drinkServiceImpl.createDrink(drink1));

    }

    @Test
    public void testUpdateTopping() throws Exception {
        Drink updatedDrink = new Drink(2L,"Latte",7.0);

        Mockito.when(drinkRepository.findById(updatedDrink.getId())).thenReturn(Optional.ofNullable(drink2));
        drink2.setName(updatedDrink.getName());
        drink2.setAmount(updatedDrink.getAmount());

        Mockito.when(drinkRepository.save(drink2)).thenReturn(drink2);
        Assertions.assertEquals(drink2,drinkServiceImpl.updateDrink(drink2));

    }

    @Test
    public void testDeleteTopping() throws Exception {
        drinkRepository.delete(drink1);
        Mockito.verify(drinkRepository,Mockito.times(1)).delete(drink1);

    }

}


