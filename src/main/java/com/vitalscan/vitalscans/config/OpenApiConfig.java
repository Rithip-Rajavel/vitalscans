package com.vitalscan.vitalscans.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VitalScans API")
                        .description("API for VitalScans application - Health metrics and appointment management system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("VitalScans Team")
                                .email("support@vitalscans.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server")
                ));
    }
}
