package com.backend.Domain.Trainer; // 실제 프로젝트 구조에 맞게 수정하세요

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByEmail(String email);

    Optional<Trainer> findByKakaoId(String kakaoId);
}
