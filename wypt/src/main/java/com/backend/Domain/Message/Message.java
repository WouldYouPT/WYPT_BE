package com.backend.Domain.Message;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Message")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 메시지 소유 트레이너 ID */
    @Column(name = "trainer_id", nullable = false)
    private Long trainerId;

    /** 메시지를 보낸(또는 받은) 회원 ID */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 20)
    private String sender;    // "trainer" or "member"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 20)
    private String direction; // "in" or "out"

    @Column(length = 20)
    private String status;    // "pending", "sent", "read", "fail" 등

    @CreationTimestamp
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;
}

