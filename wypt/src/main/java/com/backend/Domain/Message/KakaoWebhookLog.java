package com.backend.Domain.Message;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "KakaoWebhookLog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class KakaoWebhookLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 메시지를 보낸 회원 ID (Member.id) */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /** 웹훅 타입 (예: "message") */
    @Column(nullable = false, length = 50)
    private String type;

    /** 원본 페이로드를 JSON 문자열로 저장 */
    @Column(nullable = false, columnDefinition = "json")
    private String payload;

    /** 수신 시간 */
    @CreationTimestamp
    @Column(name = "recv_at", updatable = false)
    private LocalDateTime recvAt;
}


