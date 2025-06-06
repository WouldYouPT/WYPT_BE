// src/OAuth2RedirectHandler.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

export default function OAuth2RedirectHandler() {
  const navigate = useNavigate();
  const location = useLocation();
  const [tokenValue, setTokenValue] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    // 카카오 인증이 끝난 뒤 스프링이 보내주는 URL: 
    //  http://localhost:3000/oauth2/redirect?token=eyJ...
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get('token');

    if (token) {
      localStorage.setItem('jwtToken', token);
      setTokenValue(token);
    } else {
      setErrorMsg('토큰이 없습니다. 인증 흐름을 다시 시도해주세요.');
    }
  }, [location.search]);

  const goHome = () => {
    navigate('/', { replace: true });
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '100px', padding: '0 20px' }}>
      {errorMsg ? (
        <div>
          <h3 style={{ color: 'red' }}>오류 발생</h3>
          <p>{errorMsg}</p>
          <button onClick={() => navigate('/login')} style={{ marginTop: '20px', padding: '10px 20px' }}>
            로그인 페이지로 돌아가기
          </button>
        </div>
      ) : tokenValue ? (
        <div>
          <h3>로그인에 성공했습니다!</h3>
          <p>아래 토큰을 확인하세요:</p>
          <code
            style={{
              display: 'block',
              wordBreak: 'break-all',
              margin: '20px auto',
              maxWidth: '600px',
              background: '#f0f0f0',
              padding: '10px',
              borderRadius: '4px'
            }}
          >
            {tokenValue}
          </code>
          <button onClick={goHome} style={{ marginTop: '20px', padding: '10px 20px' }}>
            완료 (메인 페이지로 이동)
          </button>
        </div>
      ) : (
        <h3>OAuth2 로그인을 처리 중입니다...</h3>
      )}
    </div>
  );
}
