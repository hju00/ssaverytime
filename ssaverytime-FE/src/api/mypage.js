import http from './http'

// 내 정보 조회
export const getMyPage = () => {
  return http.get('/v1/mypage')
}

// 내 정보 수정
export const updateMyPage = (data) => {
  return http.put('/v1/mypage', data)
}

// 회원 탈퇴
export const withdraw = () => {
  return http.post('/v1/mypage')
}

// 백준 티어 갱신
export const refreshBojRank = () => {
  return http.put('/v1/mypage/boj')
}
