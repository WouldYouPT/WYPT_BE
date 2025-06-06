package com.backend.ConfigSecurity;

import com.backend.Domain.Trainer.Trainer;
import com.backend.Domain.Trainer.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TrainerRepository trainerRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        // 2) 일반 사용자 조회
        Trainer trainer = trainerRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다: " + id));

        return org.springframework.security.core.userdetails.User.builder()
                .username(trainer.getId().toString())
                .password(trainer.getPassword())
                .roles("Trainer")  // 권한 부여
                .build();
    }
}


