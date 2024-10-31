package com.damoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void addResourceHadlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(" /images/uploads/**")
                .addResourceLocations("C:\\work_damoa\\sign/")
                .setCacheControl(CacheControl.noCache());

        registry.addResourceHandler("/images/ad/**")
                .addResourceLocations("file:\\C:\\spring_jpa_work_class\\spring_damoa\\src\\main\\resources\\static\\images\\ad/");
    }


}
