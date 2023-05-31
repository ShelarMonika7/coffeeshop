package com.store.coffeeshop.utils.client.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
