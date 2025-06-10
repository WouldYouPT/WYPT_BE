package com.backend.Domain.Message;

import com.backend.Domain.Member.Member;
import com.backend.Domain.Member.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
public class KakaoWebhookController {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final KakaoWebhookLogRepository logRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> receive(@RequestBody Map<String, Object> body) {
        System.out.println("stage : 1");

        // 1) 루트 레벨 파라미터 우선
        String userKey   = body.get("userKey")   != null ? body.get("userKey").toString()   : null;
        String utterance = body.get("utterance") != null ? body.get("utterance").toString() : null;

        // 2) 루트 파라미터가 없으면 action.params 에서 꺼내오기
        if (userKey == null || utterance == null) {
            Object actionObj = body.get("action");
            if (actionObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> action = (Map<String, Object>) actionObj;
                Object paramsObj = action.get("params");
                if (paramsObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> params = (Map<String, Object>) paramsObj;
                    if (userKey   == null) userKey   = params.get("userKey")   != null ? params.get("userKey").toString()   : null;
                    if (utterance == null) utterance = params.get("utterance") != null ? params.get("utterance").toString() : null;
                }
            }
        }

        System.out.println("stage : 2, userKey=" + userKey + ", utterance=" + utterance);

        // 3) 키가 여전히 없으면 에러
        if (userKey == null || utterance == null) {
            throw new IllegalArgumentException("userKey or utterance is missing");
        }

        // 4) 회원 조회
        Member member = memberRepository.findByKakaoId(userKey)
                .orElseThrow(() -> new IllegalArgumentException("Unknown member kakaoId"));

        System.out.println("stage : 3");

        // 5) WebhookLog 저장
        try {
            String payload = objectMapper.writeValueAsString(body);
            KakaoWebhookLog log = new KakaoWebhookLog();
            log.setMemberId(member.getId());
            log.setType("message");
            log.setPayload(payload);
            logRepository.save(log);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("stage : 4");

        // 6) Message 저장
        Message msg = new Message();
        msg.setTrainerId(member.getTrainerId());
        msg.setMemberId(member.getId());
        msg.setSender("member");
        msg.setContent(utterance);
        msg.setDirection("in");
        msg.setStatus("pending");
        messageRepository.save(msg);

        System.out.println("stage : 5");

        // 7) 챗봇 응답 JSON 생성
        Map<String, Object> response = Map.of(
                "version", "2.0",
                "template", Map.of(
                        "outputs", List.of(
                                Map.of("simpleText", Map.of("text", "메시지를 잘 받았습니다 😊"))
                        )
                )
        );

        System.out.println("stage : 6");
        return ResponseEntity.ok(response);
    }
}
