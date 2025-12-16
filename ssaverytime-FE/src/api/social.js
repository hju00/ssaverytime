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
export const followUser = (userId) => {
  return http.post(`/v1/social/follow/${userId}`)
}

// 언팔로우
export const unfollowUser = (userId) => {
  return http.delete(`/v1/social/unfollow/${userId}`)
}

// 나를 팔로우한 사람
export const getFollowers = () => {
  return http.get('/v1/social/follow/to')
}

// 내가 팔로우한 사람
export const getFollowings = () => {
  return http.get('/v1/social/follow/from')
}
