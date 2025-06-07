
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import Dashboard from './components/Dashboard';

export default function App() {
  const isLoggedIn = !!localStorage.getItem('jwtToken');

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={isLoggedIn ? <Navigate to="/" replace /> : <LoginPage />}
        />
        <Route
          path="/"
          element={
            isLoggedIn
              ? <Dashboard />
              : <Navigate to="/login" replace />
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

