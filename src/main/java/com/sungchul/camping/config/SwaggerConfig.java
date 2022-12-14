package com.sungchul.camping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())           //테스트할때는 해당 내용으로
                .paths(PathSelectors.regex("/(telegram).*"))    //운영 배포시에 해당내용으로
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Camping RestAPI",
                "캠핑 예약 시스템",
                "1.0",
                "없어",
                new Contact("kimsungchul", "http://github.com/kkimsungchul", "kimsc1218@gmail.com"),
                "걍써..",
                "걍써..",
                Collections.emptyList());
    }
}