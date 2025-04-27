package com.backend.wypt.KakaoApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyMessageRequest {
    private String userKey; // 답장할 헬스회원 userKey
    private String text;    // 보낼 텍스트 내용
}