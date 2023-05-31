package com.store.coffeeshop.admin.service.impl;

import com.store.coffeeshop.exception.*;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.admin.service.ToppingService;
import com.store.coffeeshop.utils.MsgConstants;
import com.store.coffeeshop.client.model.CartItem;
import com.store.coffeeshop.client.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToppingServiceImpl implements ToppingService {

    private static final Logger logger = LoggerFactory.getLogger(ToppingServiceImpl.class);

    @Autowired
    ToppingRepository toppingRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    DrinkRepository drinkRepository;

    @Override
    public List<Topping> getAllToppings(Pageable pageable) {
        try {
            Page<Topping>  allToppings  = toppingRepository.findAll(pageable);
            List<Topping> toppings = allToppings.getContent();
            logger.info(MsgConstants.TOPPING_FETCH);
            return toppings;
        }catch (Exception e) {
            logger.error("Failed to fetch toppings: {}", e.getMessage(), e);
            throw new FailedToFetchException(MsgConstants.TOPPING_FETCH_FAILED);
        }
    }

    @Override
    public Topping getToppingById(Long id) {
        Optional<Topping> toppingOptional = toppingRepository.findById(id);
        if (toppingOptional.isPresent()) {
            Topping topping = toppingOptional.get();
            return topping;
        } else {
            logger.error("Topping not found with id: {}", id);
            throw new NotFoundException(MsgConstants.TOPPING_NOT_FOUND);        }
    }

    @Override
    public Topping createTopping(Topping topping) {
        if (topping == null) {
            logger.error("Topping is null");
            throw new BadRequestException(MsgConstants.TOPPING_NOT_NULL);        }
        try {
            topping = toppingRepository.save(topping);
            logger.info("Created new topping with id: {}", topping.getId());
            return topping;
        } catch (Exception e) {
            logger.error("Failed to create topping: {}", e.getMessage(), e);
            throw new FailedToCreateException(MsgConstants.TOPPING_CREATE_FAILED);
        }
    }

    @Override
    public Topping updateTopping(Topping topping) {

        if(topping == null || topping.getId() == null){
            logger.error("Topping or topping ID is null");
            throw new BadRequestException(MsgConstants.TOPPING_NOT_NULL);        }

        Optional<Topping> optionalTopping= toppingRepository.findById(topping.getId());
        if(!optionalTopping.isPresent()){
            logger.error("Topping not found with id: {}", topping.getId());
            throw new NotFoundException(MsgConstants.TOPPING_NOT_FOUND);        }
try {
    Topping updatedTopping = optionalTopping.get();
    updatedTopping.setName(topping.getName());
    updatedTopping.setAmount(topping.getAmount());

    updatedTopping = toppingRepository.save(updatedTopping);
    logger.info("Updated topping with id: {}", updatedTopping.getId());
    return updatedTopping;
}catch (Exception e) {
    logger.error("Failed to update topping: {}", e.getMessage(), e);
    throw new FailedToUpdateException(MsgConstants.TOPPING_UPDATE_FAILED);
}
    }

    @Override
    public void deleteTopping(Long id) {
        try {
            toppingRepository.deleteById(id);
            logger.info("Deleted topping with id: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete topping with id: {}", id, e);
            throw new FailedToDeleteException(MsgConstants.TOPPING_DELETE_FAILED);
        }

    }

    @Override
    public List<Topping> findMostUsedToppingForDrink(Long drinkId) {
        try {
            Drink drink = drinkRepository.findById(drinkId)
                    .orElseThrow(() -> new NotFoundException("Drink not found"));

            List<CartItem> cartItems = cartItemRepository.findByDrink(drink);

            // Create a map to store the topping IDs and their occurrence counts
            Map<Long, Integer> toppingCounts = new HashMap<>();

            // Iterate over the cart items and count the occurrences of each topping
            for (CartItem cartItem : cartItems) {
                List<Topping> toppings = cartItem.getToppings();
                for (Topping topping : toppings) {
                    Long toppingId = topping.getId();
                    toppingCounts.put(toppingId, toppingCounts.getOrDefault(toppingId, 0) + 1);
                }
            }

            // Sort the topping counts in descending order of occurrence count
            List<Map.Entry<Long, Integer>> sortedCounts = new ArrayList<>(toppingCounts.entrySet());
            sortedCounts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Retrieve the most used topping IDs (limit to top 3)
            List<Long> mostUsedToppingIds = sortedCounts.stream()
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // Retrieve the actual Topping entities using the IDs
            List<Topping> mostUsedToppings = toppingRepository.findAllById(mostUsedToppingIds);

            return mostUsedToppings;
        }catch (Exception e) {
            logger.error("Failed to find most used topping for drink with id: {}", drinkId, e);
            throw new FailedToFetchException(MsgConstants.MOST_USED_TOPPING_FETCH_FAILED);
        }

    }
}



