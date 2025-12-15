import http from './http'

// 게시글 목록 조회
export const getBoardList = (params) => {
  return http.get('/v1/board', { params })
}

// 게시글 상세 조회
export const getBoardDetail = (boardId) => {
  return http.get(`/v1/board/${boardId}`)
}

// 게시글 작성
export const writeBoard = (data) => {
  return http.post('/v1/board', data)
}

// 게시글 수정
export const updateBoard = (data) => {
  return http.put('/v1/board', data)
}

// 게시글 삭제
export const deleteBoard = (boardId) => {
  return http.delete(`/v1/board/${boardId}`)
}

// 좋아요 토글
export const toggleLike = (boardId) => {
  return http.post(`/v1/board/${boardId}/like`)
}

// 스크랩 토글
export const toggleScrap = (boardId) => {
  return http.post(`/v1/board/${boardId}/scrap`)
}

// 내 스크랩 목록 조회
export const getScrapList = (params) => {
  return http.get('/v1/board/scrap', { params })
}

// AI 요약
export const getAiSummary = (boardId) => {
  return http.post(`/v1/board/${boardId}/ai`)
}
