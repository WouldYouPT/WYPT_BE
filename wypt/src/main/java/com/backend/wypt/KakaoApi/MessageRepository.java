package com.backend.wypt.KakaoApi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // 특정 회원(userKey) 기준으로 메시지 조회
    List<MessageEntity> findByUserKeyOrderByCreatedAtAsc(String userKey);
}