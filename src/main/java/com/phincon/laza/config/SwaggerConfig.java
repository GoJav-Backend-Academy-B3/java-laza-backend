package com.phincon.laza.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server URL"),
                @Server(url = "https://api.lazaapp.shop", description = "Deploy Server URL")
        }
)
public class SwaggerConfig {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer X-AUTH-TOKEN"))
                .components(new Components().addSecuritySchemes
                        ("Bearer X-AUTH-TOKEN", createAPIKeyScheme()))
                .info(new Info().title("Laza REST API")
                        .description("Collection of Laza Backend API.")
                        .version("1.0").contact(new Contact().name("Adimas Putra P")
                                .email("api.lazaapp.shop").url("admin@lazaaap.shop"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}
