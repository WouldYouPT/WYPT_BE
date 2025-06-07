// src/components/LoginPage.jsx
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { kakaoLogin } from '../api/auth';

const KAKAO_JS_KEY = '0f357085b99c399a59495bb041ca191d'; 

export default function LoginPage() {
  const navigate = useNavigate();

  useEffect(() => {
    if (window.Kakao && !window.Kakao.isInitialized()) {
      window.Kakao.init(KAKAO_JS_KEY);
      console.log('✅ Kakao SDK initialized:', window.Kakao.isInitialized());
    }
  }, []);

  const handleKakaoLogin = () => {
    if (!window.Kakao) {
      alert('카카오 SDK가 로드되지 않았습니다. 새로고침 후 다시 시도해주세요.');
      return;
    }

    window.Kakao.Auth.login({
      scope: 'profile_nickname,profile_image,account_email',
      success: async ({ access_token }) => {
        try {
          const { token } = await kakaoLogin(access_token);
          localStorage.setItem('jwtToken', token);
          navigate('/', { replace: true });
        } catch (err) {
          console.error(err);
          alert('소셜 로그인 중 오류가 발생했습니다.');
        }
      },
      fail: (err) => {
        console.error(err);
        alert('카카오 인증에 실패했습니다.');
      }
    });
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '100px' }}>
      <h2>소셜 로그인</h2>
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
        카카오 로그인
      </button>
    </div>
  );
}
