package com.resume.resume_parser.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Resume Parser API")
                    .version("1.0.0")
                    .description("API documentation for Resume Parser project")
                    .contact(new Contact()
                        .name("Praveen Bhutani")
                        .email("bhutanipraveen74@gmail.com")
                        )
                    .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("All Controllers")
                .pathsToMatch("/**") 
                .build();
    }
}
