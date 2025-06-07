package com.backend.Domain.Login.SocialLogin;

import com.backend.ConfigSecurity.JwtTokenProvider;
import com.backend.Domain.Login.Dto.LoginResponseDto;
import com.backend.Domain.Trainer.Trainer;
import com.backend.Domain.Trainer.TrainerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialService {
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final TrainerRepository trainerRepository;
    private final JwtTokenProvider jwtProvider;
    private final WebClient webClient;

    @Transactional
    public LoginResponseDto loginWithKakao(String accessToken) {
        // 1) Map 형태로 유저 정보 호출
        Map<String, Object> attributes = webClient.get()
                .uri(KAKAO_USERINFO_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        // 2) 도메인 객체로 변환
        KakaoUserInfo info = new KakaoUserInfo(attributes);

        // 3) 회원 조회 및 가입
        String kakaoId = info.getKakaoId();
        Trainer m = trainerRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Trainer mm = Trainer.builder()
                            .kakaoId(kakaoId)
                            .email(info.getEmail())
                            .name(info.getNickname())
                            .build();
                    return trainerRepository.save(mm);
                });

        // 4) JWT 생성 및 반환
        String jwt = jwtProvider.createToken(m.getId(), "trainer");
        return LoginResponseDto.builder()
                .token(jwt)
                .type("trainer")
                .build();
    }
}
