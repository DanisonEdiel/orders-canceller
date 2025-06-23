package com.example.ordercanceler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderCancelerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderCancelerApplication.class, args);
    }

}
