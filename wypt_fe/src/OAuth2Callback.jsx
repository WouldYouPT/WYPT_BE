// src/OAuth2Callback.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

export default function OAuth2Callback() {
  const navigate = useNavigate();
  const location = useLocation();
  const [tokenValue, setTokenValue] = useState(null);
  const [errorMsg, setErrorMsg] = useState(null);

  useEffect(() => {
    // URL 쿼리 파라미터에서 code, state 꺼내기
    const queryParams = new URLSearchParams(location.search);
    const code = queryParams.get('code');
    const state = queryParams.get('state');

    if (!code) {
      // code가 없으면 로그인으로 돌아가기
      setErrorMsg('인가 코드가 없습니다. 로그인부터 다시 시도하세요.');
      return;
    }

    // 백엔드의 콜백 엔드포인트를 호출해서 JSON({ token, type })을 받아 온다
    fetch(`http://localhost:8080/oauth2/callback/kakao?code=${code}&state=${state}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      }
    })
      .then(async (res) => {
        if (!res.ok) {
          // 에러가 발생하면 오류 메시지를 JSON으로 읽어 봄 (예: 400/401)
          const text = await res.text();
          throw new Error(text || '토큰 발급 실패');
        }
        return res.json();
      })
      .then((data) => {
        // data: { token: "...", type: "trainer" }
        const { token } = data;
        if (token) {
          // 1) localStorage에 토큰 저장
          localStorage.setItem('jwtToken', token);
          // 2) 상태로 저장해서 화면에 렌더
          setTokenValue(token);
        } else {
          setErrorMsg('토큰이 응답에 없습니다.');
        }
      })
      .catch((err) => {
        setErrorMsg(`백엔드 호출 중 오류: ${err.message}`);
      });
  }, [location.search, navigate]);

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
          <button onClick={() => navigate('/')} style={{ marginTop: '20px', padding: '10px 20px' }}>
            완료 (메인 페이지로 이동)
          </button>
        </div>
      ) : (
        <h3>OAuth2 로그인을 처리 중입니다...</h3>
      )}
    </div>
  );
}
