import http from './http'

// 게시글 목록 조회
export const getBoardList = (params) => {
  return http.get('/board', { params })
}

// 게시글 상세 조회
export const getBoardDetail = (boardId) => {
  return http.get(`/board/${boardId}`)
}

// 게시글 작성
export const writeBoard = (data) => {
  return http.post('/board', data)
}

// 게시글 수정
export const updateBoard = (data) => {
  return http.put('/board', data)
}

// 게시글 삭제
export const deleteBoard = (boardId) => {
  return http.delete(`/board/${boardId}`)
}

// 좋아요 토글
export const toggleLike = (boardId) => {
  return http.post(`/board/${boardId}/like`)
}

// 스크랩 토글
export const toggleScrap = (boardId) => {
  return http.post(`/board/${boardId}/scrap`)
}
