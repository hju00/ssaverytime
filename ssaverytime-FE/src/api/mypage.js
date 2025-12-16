import http from './http'

// 내 정보 조회
export const getMyPage = () => {
  return http.get('/v1/mypage')
}

// 내 정보 수정 (password, name, season)
export const updateMyPage = (data) => {
  return http.put('/v1/mypage', data)
}

// 회원 탈퇴 (VALID -> INVALID)
export const withdraw = () => {
  return http.post('/v1/mypage')
}

// 백준 티어 갱신 (성공 시 { baekjoon: svgUrl } 반환)
export const refreshBojRank = () => {
  return http.put('/v1/mypage/boj')
}
