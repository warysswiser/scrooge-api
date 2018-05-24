package com.warys.scrooge.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.swagger.api.title}")
    private String title;
    @Value("${app.swagger.api.description}")
    private String description;
    @Value("${app.swagger.api.version}")
    private String version;
    @Value("${app.swagger.api.termsOfService}")
    private String termsOfService;
    @Value("${app.swagger.api.contact.name}")
    private String contactName;
    @Value("${app.swagger.api.license.name}")
    private String license;
    @Value("${app.swagger.api.license.url}")
    private String licenseUrl;
    @Value("${app.swagger.api.contact.url}")
    private String contactUrl;
    @Value("${app.swagger.api.contact.email}")
    private String contactEmail;
    @Value("${app.swagger.basePackage}")
    private String basePackage;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                title,
                description,
                version,
                termsOfService,
                new Contact(contactName, contactUrl, contactEmail),
                license, licenseUrl, Collections.emptyList());
    }
}