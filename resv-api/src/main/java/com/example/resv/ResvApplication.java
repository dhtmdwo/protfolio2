package com.example.resv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories(basePackages = {
//        "com.example.appapi.store",
//        "com.example.resv.resv"})
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.appapi","com.example.resv"})
public class ResvApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResvApplication.class, args);
    }

}
