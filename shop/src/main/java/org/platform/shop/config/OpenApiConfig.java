package org.platform.shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shopApi() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Shop API")
                                .description("Sample Ecommerce API")
                                .version("1.0.0")
                );
    }
}