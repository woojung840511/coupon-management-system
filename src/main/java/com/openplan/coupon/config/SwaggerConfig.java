package com.openplan.coupon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("쿠폰 관리 시스템 API")
                .description("쿠폰의 발급, 사용, 조건 관리 등을 위한 API")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("우정")
                    .email("woojung840511@gmail.com")));
    }
}
