package edu.csumb.spring19.capstone.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableSwagger2
public class Swagger {
    @Value("${ENABLE_SWAGGER:false}")
    private Boolean docsEnabled;

    @Bean
    public Docket api() {
        if (docsEnabled) {
            return new Docket(DocumentationType.SWAGGER_2)
                  .select()
                  .apis(RequestHandlerSelectors.basePackage("edu.csumb.spring19.capstone"))
                  .paths(Predicates.not(PathSelectors.regex("/error.*")))
                  .build()
                  .securitySchemes(securitySchemes());
        } else {
            return new Docket(DocumentationType.SWAGGER_2).select().build();
        }
    }

    private static ArrayList<? extends SecurityScheme> securitySchemes() {
        return new ArrayList<>(Arrays.asList(new ApiKey("Bearer", "Authorization", "header")));
    }
}

