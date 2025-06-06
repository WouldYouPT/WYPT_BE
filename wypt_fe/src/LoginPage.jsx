// src/LoginPage.jsx
import React from 'react';

const API_BASE_URL = 'http://localhost:8080'; // 백엔드 주소(로컬 테스트용)

export default function LoginPage() {
  const handleKakaoLogin = () => {
    // 이 URL만 누르면, 곧바로 Spring Security → 카카오 인증 화면으로 넘어갑니다.
    window.location.href = `${API_BASE_URL}/oauth2/authorization/kakao`;
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '100px' }}>
      <h2>로그인 페이지</h2>
      <button
        onClick={handleKakaoLogin}
        style={{
          padding: '12px 24px',
          backgroundColor: '#FEE500',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer',
          fontSize: '16px'
        }}
      >
        카카오로 로그인
      </button>
    </div>
  );
}
