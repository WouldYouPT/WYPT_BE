package com.backend.ConfigSecurity.RefreshToken;

import com.backend.Domain.Trainer.Trainer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTrainer(Trainer trainer);
    Optional<RefreshToken> findByToken(String token);
    void deleteByTrainer(Trainer trainer);
}

