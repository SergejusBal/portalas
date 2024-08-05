package Paskaita_2024_08_05PostPortalas.portalas.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Allow all origins
                        .allowedOrigins("http://localhost:3000", "http://127.0.0.1:5500/") // Specific origins if needed
                        .allowedMethods("GET", "POST")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Pastatyti true jei reikes tiksliu adresu
            }
        };
    }
}