package com.kefas.EWallet_aop.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
public class BeansConfiguration {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info().title("EWallet_aop").version("snapshot")
                        .description("""
                                <div><h1>A Wallet API Integrated with Paystack.</h1></div>
                                <h3><b><u>Quick guide</u></b></h3>
                                <div>1. On successful registration. Kindly cjeck your email to get an activation code needed to activate your account.</div>
                                <div>2. Next, login with your email and password. To get a JWT token.</div>
                                <div>3. Then, copy JWT token returned and paste at the top right corner button called Authorize.</div>
                                <div>4. You can now proceed with any endpoint call that is protected.</div>
                                """))
                .addServersItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }

    @Bean
    public ObjectMapper getMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }
}
