import axios from 'axios';

const API_BASE = 'http://localhost:8080/v1/auth';

export async function kakaoLogin(accessToken) {
  const { data } = await axios.post(
    `${API_BASE}/social/kakao`,
    { accessToken },
    { headers: { 'Content-Type': 'application/json' } }
  );
  return data; // { token, type }
}

