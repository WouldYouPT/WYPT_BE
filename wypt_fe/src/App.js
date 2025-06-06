// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './LoginPage';
import OAuth2RedirectHandler from './OAuth2RedirectHandler';

export default function App() {
  const isLoggedIn = !!localStorage.getItem('jwtToken');

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={isLoggedIn ? <Navigate to="/" replace /> : <LoginPage />}
        />
        <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
        <Route
          path="/"
          element={
            isLoggedIn ? (
              <div style={{ textAlign: 'center', marginTop: '100px' }}>
                <h2>이미 로그인 상태입니다</h2>
                <p>
                  저장된 JWT: <code style={{ wordBreak: 'break-all' }}>{localStorage.getItem('jwtToken')}</code>
                </p>
              </div>
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />
        <Route
          path="*"
          element={isLoggedIn ? <Navigate to="/" replace /> : <Navigate to="/login" replace />}
        />
      </Routes>
    </Router>
  );
}
