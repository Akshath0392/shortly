package com.akshath.shortly.configs;

import com.akshath.shortly.connectors.HashKey;
import com.akshath.shortly.repository.Shortly;
import com.akshath.shortly.repository.ShortlyRepository;
import com.akshath.shortly.services.ShortlyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AppConfig {
    
    @Bean
    ShortlyService shortlyService() {
        return new ShortlyService();
    }

    @Bean
    @Scope("prototype")
    ShortlyRepository shortlyRepository() {
        return new ShortlyRepository();
    }

    @Bean
    HashKey hashkey() {
        return new HashKey();
    }

    @Bean
    Shortly shortly() {
        return new Shortly();
    }
}
