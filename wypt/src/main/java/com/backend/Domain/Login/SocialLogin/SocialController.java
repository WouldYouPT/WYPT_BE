package com.backend.Domain.Login.SocialLogin;

import com.backend.Domain.Login.Dto.LoginResponseDto;
import com.backend.Domain.Login.Dto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;

    @PostMapping("/social/kakao")
    public ResponseEntity<LoginResponseDto> kakaoSocialLogin(@RequestBody SocialLoginRequestDto dto) {
        LoginResponseDto resp = socialService.loginWithKakao(dto.getAccessToken());
        return ResponseEntity.ok(resp);
    }
}
