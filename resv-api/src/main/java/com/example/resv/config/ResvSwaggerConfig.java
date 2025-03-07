package com.example.resv.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ResvSwaggerConfig {
    @Bean
    public OpenAPI ResvOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .servers(List.of(new Server().url("http://localhost:8080")))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Eatzzy 식당 예약 API 명세서")
                .description("식당 예약 관련 API 명세서 페이지입니다.<br><br>")
                .version("1.0.0");
    }
}