package com.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // 기본 설정 (타임아웃, baseUrl 등 필요시 여기서 추가 가능)
        return builder.build();
    }
}
