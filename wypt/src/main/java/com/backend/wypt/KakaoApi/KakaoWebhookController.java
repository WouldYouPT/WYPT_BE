package com.backend.wypt.KakaoApi;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook/kakao")
public class KakaoWebhookController {

    private final MessageRepository messageRepository;
    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/{userKey}")
    public ResponseEntity<List<MessageEntity>> getMessagesByUserKey(@PathVariable String userKey) {
        List<MessageEntity> messages = messageRepository.findByUserKeyOrderByCreatedAtAsc(userKey);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        System.out.println("카카오에서 받은 전체 데이터: " + payload);

        try {
            String userKey = (String) payload.get("userKey");
            Map<String, Object> message = (Map<String, Object>) payload.get("message");

            if (message != null) {
                String messageType = (String) message.get("type");

                MessageEntity chatMessage = new MessageEntity();
                chatMessage.setUserKey(userKey);
                chatMessage.setMessageType(messageType);

                if ("text".equals(messageType)) {
                    chatMessage.setContent((String) message.get("text"));
                } else if ("image".equals(messageType)) {
                    chatMessage.setImageUrl((String) message.get("imageUrl"));
                }

                messageRepository.save(chatMessage); // DB에 저장
                System.out.println("메시지 저장 완료: " + chatMessage);
            }
            return ResponseEntity.ok("ok");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyToUser(@RequestBody ReplyMessageRequest request) {
        try {
            // AccessToken 발급

            String accessToken = kakaoAuthService.getAccessToken();

            // 메시지 전송
            KakaoMessageService.sendTextMessage(request.getUserKey(), request.getText(), accessToken);

            return ResponseEntity.ok("메시지 전송 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("메시지 전송 실패");
        }
    }

}
