package com.store.coffeeshop.admin.service.impl;

import com.store.coffeeshop.exception.*;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.service.DrinkService;
import com.store.coffeeshop.utils.MsgConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkServiceImpl implements DrinkService {

    private static final Logger logger = LoggerFactory.getLogger(DrinkServiceImpl.class);

    @Autowired
    DrinkRepository drinkRepository;

    @Override
    public List<Drink> getAllDrinks(Pageable pageable) {
        try {
            Page<Drink> allDrinks = drinkRepository.findAll(pageable);
            List<Drink> drinks = allDrinks.getContent();
            logger.info(MsgConstants.DRINK_FETCH);
            return drinks;
        }catch (Exception e) {
            logger.error("Failed to fetch drinks: {}", e.getMessage(), e);
            throw new FailedToFetchException(MsgConstants.DRINK_FETCH_FAILED);
        }
    }

    @Override
    public Drink getDrinkById(Long id) {
        Optional<Drink> drinkOptional = drinkRepository.findById(id);
        if (drinkOptional.isPresent()) {
            Drink drink = drinkOptional.get();
            return drink;
        } else {
            logger.error("Drink not found with id: {}", id);
            throw new NotFoundException(MsgConstants.DRINK_NOT_FOUND);
        }
    }

    @Override
    public Drink createDrink(Drink drink) {
        if (drink == null) {
            logger.error("Drink is null");
            throw new BadRequestException(MsgConstants.DRINK_NOT_NULL);        }
        try {
            drink = drinkRepository.save(drink);
            logger.info("Created new drink with id: {}", drink.getId());
            return drink;
        } catch (Exception e) {
            logger.error("Failed to create drink: {}", e.getMessage(), e);
            throw new FailedToCreateException(MsgConstants.DRINK_CREATE_FAILED);
        }
    }

    @Override
    public Drink updateDrink(Drink drink) {

        if(drink == null || drink.getId() == null){
            logger.error("Drink or drink ID is null");
            throw new BadRequestException(MsgConstants.DRINK_NOT_NULL);        }

        Optional<Drink> optionalDrink = drinkRepository.findById(drink.getId());
        if(!optionalDrink.isPresent()){
            logger.error("Drink not found with id: {}", drink.getId());
            throw new NotFoundException(MsgConstants.DRINK_NOT_FOUND);        }

        try {
            Drink updatedDrink = optionalDrink.get();
            updatedDrink.setName(drink.getName());
            updatedDrink.setAmount(drink.getAmount());

            updatedDrink = drinkRepository.save(updatedDrink);
            logger.info("Updated drink with id: {}", updatedDrink.getId());
            return updatedDrink;
        }catch (Exception e) {
            logger.error("Failed to update drink: {}", e.getMessage(), e);
            throw new FailedToUpdateException(MsgConstants.DRINK_UPDATE_FAILED);
        }
    }

    @Override
    public void deleteDrink(Long id) {
        try {
            drinkRepository.deleteById(id);
            logger.info("Deleted drink with id: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete drink with id: {}", id, e);
            throw new FailedToDeleteException(MsgConstants.DRINK_DELETE_FAILED);
        }

    }
}


