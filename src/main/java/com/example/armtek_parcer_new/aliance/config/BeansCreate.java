package com.example.armtek_parcer_new.aliance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeansCreate {

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(10);
    }
}
