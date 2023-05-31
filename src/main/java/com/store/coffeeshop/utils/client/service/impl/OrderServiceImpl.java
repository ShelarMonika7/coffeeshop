package com.store.coffeeshop.utils.client.service.impl;

import com.store.coffeeshop.utils.client.repository.model.Cart;
import com.store.coffeeshop.utils.client.repository.model.CartItem;
import com.store.coffeeshop.utils.client.repository.model.Order;
import com.store.coffeeshop.utils.client.repository.model.dto.DrinkDTO;
import com.store.coffeeshop.utils.client.repository.model.dto.DrinkReques;
import com.store.coffeeshop.utils.client.repository.model.dto.OrderRequest;
import com.store.coffeeshop.utils.client.repository.model.dto.OrderResponse;
import com.store.coffeeshop.exception.BadRequestException;
import com.store.coffeeshop.exception.FailedToCalculateException;
import com.store.coffeeshop.exception.FailedToCreateException;
import com.store.coffeeshop.admin.model.Drink;
import com.store.coffeeshop.admin.model.Topping;
import com.store.coffeeshop.admin.repository.DrinkRepository;
import com.store.coffeeshop.admin.repository.ToppingRepository;
import com.store.coffeeshop.utils.MsgConstants;
import com.store.coffeeshop.utils.client.repository.CartItemRepository;
import com.store.coffeeshop.utils.client.repository.CartRepository;
import com.store.coffeeshop.utils.client.repository.OrderRepository;
import com.store.coffeeshop.utils.client.service.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    DrinkRepository drinkRepository;

    @Autowired
    ToppingRepository toppingRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        try {
            List<DrinkDTO> drinks = orderRequest.getDrinks().stream()
                    .map(this::createDrinkDTO)
                    .collect(Collectors.toList());

        double cartAmount = drinks.stream()
                .mapToDouble(DrinkDTO::getTotalAmount)
                .sum();

        double discountedAmount = calculateDiscountedAmount(cartAmount, drinks);

        List<CartItem> cartItems = drinks.stream()
                .map(drinkDTO -> createCartItem(drinkDTO))
                .collect(Collectors.toList());

        Cart cart = new Cart();
        cart.setTotalCost(cartAmount);
        cart.setDiscountedAmount(discountedAmount);
        cart.setCartItems(cartItems);
        cartRepository.save(cart);

        cartItems.forEach(cartItem -> {
            cartItem.setCart(cart);
            cartItemRepository.save(cartItem);
        });

        Order order = new Order();
        order.setCart(cart);
        orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCartAmount(cartAmount);
        orderResponse.setDiscountedAmount(discountedAmount);
        orderResponse.setDrinks(drinks);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }catch (Exception e) {
            logger.error("Failed to create order: {}", e.getMessage(), e);
            throw new FailedToCreateException(MsgConstants.ORDER_CREATE_FAILED);
        }
    }

    private DrinkDTO createDrinkDTO(DrinkReques drinkReques){
        try {
            Drink drink = drinkRepository.getDrinkByName(drinkReques.getName())
                    .orElseThrow(() -> new BadRequestException("Invalid drink: " + drinkReques.getName()));

            List<Topping> toppings = drinkReques.getToppings().stream()
                    .map(toppingName -> toppingRepository.getToppingByName(toppingName)
                            .<BadRequestException>orElseThrow(() -> new BadRequestException("Invalid topping: " + toppingName)))
                    .collect(Collectors.toList());

            double totalAmount = toppings.stream()
                    .mapToDouble(Topping::getAmount)
                    .sum() + drink.getAmount();

            DrinkDTO drinkDTO = modelMapper.map(drink, DrinkDTO.class);
            drinkDTO.setToppings(toppings);
            drinkDTO.setTotalAmount(totalAmount);
            return drinkDTO;
        }catch (Exception e) {
            logger.error("Failed to create DrinkDTO: {}", e.getMessage(), e);
            throw new FailedToCreateException(MsgConstants.ORDER_CREATE_DRINK_DTO_FAILED);
        }

    }

    private CartItem createCartItem(DrinkDTO drinkDTO) {
        try {
            CartItem cartItem = new CartItem();
            Drink drink = modelMapper.map(drinkDTO, Drink.class);
            cartItem.setDrink(drink);
            cartItem.setToppings(drinkDTO.getToppings());

            return cartItem;
        }catch (Exception e) {
            logger.error("Failed to create CartItem: {}", e.getMessage(), e);
            throw new FailedToCreateException(MsgConstants.ORDER_CREATE_CART_ITEM_FAILED);
        }
    }

    private double calculateDiscountedAmount(double cartAmount, List<DrinkDTO> drinks) {
        try {
            if (cartAmount > 12 && drinks.size() >= 3) {
                double discountedAmountWithDiscount = cartAmount - (cartAmount * 0.25);
                double amountWithOneDrinkFree = cartAmount - drinks.get(0).getTotalAmount();
                return Math.min(discountedAmountWithDiscount, amountWithOneDrinkFree);
            } else if (cartAmount > 12) {
                return cartAmount - (cartAmount * 0.25);
            } else if (drinks.size() >= 3) {
                return cartAmount - drinks.get(0).getTotalAmount();
            } else {
                return cartAmount;
            }
        }catch (Exception e) {
            logger.error("Failed to calculate discounted amount: {}", e.getMessage(), e);
            throw new FailedToCalculateException(MsgConstants.FAILED_TO_CALCULATE);
        }
    }
}


