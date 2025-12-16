import axios from 'axios'

// ✅ Vite proxy 경유용
const http = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

http.interceptors.request.use(
  (config) => {
    // skipAuth 요청이면 Authorization 제거
    if (config.skipAuth === true) {
      config.headers = config.headers || {}
      delete config.headers.Authorization
      return config
    }

    // 기본: 토큰 있으면 Authorization 추가
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

http.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default http
