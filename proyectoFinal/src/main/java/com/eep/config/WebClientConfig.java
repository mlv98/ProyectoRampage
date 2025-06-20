// src/main/java/com/eep/config/WebClientConfig.java
package com.eep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient rawgWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.rawg.io/api")
                .build();
    }
}
