package com.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Planner API")
                        .version("1.0")
                        .description("API documentation for the Planner application.")
                        .contact(new Contact().name("Hítalo").email("hitalolima173@gmail.com")
                                .url("https://hitalo-lima.github.io/devlinks/")));
    }
}
