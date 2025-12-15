import axios from 'axios'

// 백엔드 API 주소 (Vite Proxy 사용)
const BASE_URL = '/api'

const http = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청 인터셉터 (필요시 토큰 추가 등)
http.interceptors.request.use(
  (config) => {
    // ✅ 이 요청은 토큰을 붙이지 않음 (예: 회원가입/백준검증)
    if (config.skipAuth === true) {
      if (config.headers && config.headers.Authorization) {
        delete config.headers.Authorization
      }
      return config
    }

    // ✅ 기본: 토큰 있으면 Authorization 추가
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 응답 인터셉터 (에러 처리 등)
http.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default http
