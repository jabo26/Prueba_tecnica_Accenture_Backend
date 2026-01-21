package com.accenture.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Clase principal de la aplicación Spring Boot
 * 
 * @SpringBootApplication: Combina @Configuration, @EnableAutoConfiguration y @ComponentScan
 * @EnableR2dbcRepositories: Habilita los repositorios reactivos
 */
@SpringBootApplication
@EnableR2dbcRepositories
public class FranquiciasApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(FranquiciasApiApplication.class, args);
    }
}
