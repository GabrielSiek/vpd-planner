package com.vpd.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }

    //API publica
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // aplica para todos os endpoints
                .allowedOrigins("*") // aceita requisições de qualquer origem
                .allowedMethods("*") // permite todos os métodos HTTP
                .allowedHeaders("*") // permite todos os headers
                .allowCredentials(false); // precisa ser false se allowedOrigins == "*"
    }
}
