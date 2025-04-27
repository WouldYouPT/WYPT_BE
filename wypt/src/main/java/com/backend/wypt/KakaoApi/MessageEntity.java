package com.backend.wypt.KakaoApi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userKey; // 카카오 유저 식별 키

    @Column(nullable = false)
    private String messageType; // "text" 또는 "image"

    @Column(columnDefinition = "TEXT")
    private String content; // 텍스트 내용 (text 타입일 때)

    @Column(columnDefinition = "TEXT")
    private String imageUrl; // 이미지 URL (image 타입일 때)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 수신 시각

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

