package com.sergi.hellofunc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@SpringBootApplication
public class HelloFuncApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloFuncApplication.class, args);
    }

    @Bean
    public Function<String, String> hello() {
        return payload -> payload;
    }

    @Bean
    public Function<Flux<String>, Flux<String>> readList() {
        return flux -> flux.map(value -> value.toUpperCase());
    }

}
