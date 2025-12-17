import http from './http'

// 백준 ID로 사용자 검색
export const searchUserByBojId = (bojId) => {
  return http.get(`/v1/social/bojId/${bojId}`)
}

// 이름으로 사용자 검색
export const searchUserByName = (name) => {
  return http.get(`/v1/social/name/${name}`)
}

// 팔로우
export const followUser = (bojId) => {
  return http.post(`/v1/social/follow/${bojId}`)
}

// 언팔로우 (DELETE /follow/{bojId})
export const unfollowUser = (bojId) => {
  return http.delete(`/v1/social/follow/${bojId}`)
}

// 내가 팔로우한 사람(팔로잉)
export const getFollowings = () => {
  return http.get('/v1/social/follow/to')
}

// 나를 팔로우한 사람(팔로워)
export const getFollowers = () => {
  return http.get('/v1/social/follow/from')
}
