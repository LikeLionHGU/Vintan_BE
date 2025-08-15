package com.vintan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class VintanBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VintanBeApplication.class, args);
    }

}
