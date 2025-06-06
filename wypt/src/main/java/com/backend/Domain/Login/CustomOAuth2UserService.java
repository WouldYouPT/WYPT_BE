package com.backend.Domain.Login;

import com.backend.ConfigSecurity.JwtTokenProvider;
import com.backend.ConfigSecurity.RefreshToken.RefreshTokenService;
import com.backend.Domain.Trainer.Trainer;
import com.backend.Domain.Trainer.TrainerRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomOAuth2UserService에서는
 * 1) 카카오 프로필을 받아서 Trainer 조회/가입
 * 2) JWT Access/Refresh 토큰을 생성하고,
 * 3) RefreshTokenService.saveOrUpdateToken(...) 호출로 DB에 저장/업데이트
 * 4) DefaultOAuth2User의 attributes에 accessToken을 담아 반환
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final TrainerRepository trainerRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomOAuth2UserService(TrainerRepository trainerRepository,
            RefreshTokenService refreshTokenService, JwtTokenProvider jwtTokenProvider) {
        this.trainerRepository = trainerRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1) DefaultOAuth2UserService 통해 카카오 프로필 조회
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2) registrationId 검증 (kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!"kakao".equalsIgnoreCase(registrationId)) {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 Provider: " + registrationId);
        }

        // 3) KakaoUserInfo로 필요한 정보 파싱
        Map<String, Object> attributes = oAuth2User.getAttributes();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

        // 4) Trainer 조회/가입
        String kakaoId = kakaoUserInfo.getKakaoId();
        Trainer trainer = trainerRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> registerNewTrainer(kakaoUserInfo));

        // 5) JWT 토큰 생성 (Access + Refresh)
        String accessToken = jwtTokenProvider.createToken(trainer.getId(), "trainer");
        String refreshToken = jwtTokenProvider.createRefreshToken(trainer.getId(), "trainer");

        // 6) RefreshTokenService.saveOrUpdateToken(...) 호출
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(3); // 예: 3일 후 만료
        refreshTokenService.saveOrUpdateToken(trainer, refreshToken, expiryDate);

        // 7) attributes 복사 후 accessToken만 추가 (프론트에서 refreshToken은 별도 호출 시 사용)
        Map<String, Object> mappedAttributes = new HashMap<>(attributes);
        mappedAttributes.put("accessToken", accessToken);

        // 8) DefaultOAuth2User 반환 (ROLE_Trainer + 토큰 포함)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_Trainer")),
                mappedAttributes,
                "id"
        );
    }

    // 신규 카카오 사용자(Trainer) 생성 메서드
    private Trainer registerNewTrainer(KakaoUserInfo kakaoUserInfo) {
        Trainer newTrainer = new Trainer();
        newTrainer.setName(kakaoUserInfo.getNickname());

        String email = kakaoUserInfo.getEmail();
        if (email == null || email.isEmpty()) {
            email = "kakao_" + kakaoUserInfo.getKakaoId() + "@noemail.com";
        }
        newTrainer.setEmail(email);

        newTrainer.setPassword(UUID.randomUUID().toString());
        newTrainer.setKakaoId(kakaoUserInfo.getKakaoId());
        return trainerRepository.save(newTrainer);
    }
}
