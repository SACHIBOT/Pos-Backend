package com.pos.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("POS Backend API")
                        .version("1.0")
                        .description("API documentation for POS Backend. \n\n" +
                                "### Security Breakdown:\n" +
                                "- **Public Access**: No authentication required for `/auth/**`, `/swagger-ui/**`, `/v3/**`.\n"
                                +
                                "- **Admin-only Access**: Requires `ADMIN` role for `/admin/**`.\n" +
                                "- **Manager and Admin Access**: Requires `MANAGER` or `ADMIN` role for `/manager/**`.\n"
                                +
                                "- **Cashier, Manager and Admin Access**: Requires either `CASHIER` or `MANAGER` or `Admin` role for all other requests."))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(Type.HTTP)
                                        .scheme("bearer")
                                        .in(In.HEADER)
                                        .name("Authorization")));
    }
}