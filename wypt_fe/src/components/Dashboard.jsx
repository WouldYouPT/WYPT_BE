// src/components/Dashboard.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const navigate = useNavigate();
  const token = localStorage.getItem('jwtToken');

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    navigate('/login', { replace: true });
  };

  return (
    <div style={{ textAlign: 'center', padding: '20px' }}>
      <h1>메인 페이지</h1>
      {token ? (
        <>
          <p>로그인에 성공하셨습니다. 🎉</p>
          <p><strong>저장된 JWT:</strong></p>
          <code style={{ wordBreak: 'break-all' }}>{token}</code>
          <br />
          <button onClick={handleLogout} style={{ marginTop: '20px', padding: '10px 20px' }}>
            로그아웃
          </button>
        </>
      ) : (
        <p>로그인 상태가 아닙니다.</p>
      )}
    </div>
  );
}
