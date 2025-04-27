package com.backend.wypt.KakaoApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


@Service
@RequiredArgsConstructor
public class KakaoMessageService {

    private final RestTemplate restTemplate;

    private final String KAKAO_API_URL = "https://api.kakaobusiness.com/v1/messages";

    public static void sendTextMessage(String userKey, String text, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> template = new HashMap<>();
        template.put("outputs", List.of(Map.of(
                "simpleText", Map.of("text", text)
        )));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userKey", userKey);
        requestBody.put("template", template);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_API_URL, entity, String.class);
        System.out.println("카카오 응답: " + response.getBody());
    }
}
