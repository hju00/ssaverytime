import http from './http'

// 댓글 목록 조회
export const getCommentList = (boardId) => {
  return http.get(`/v1/board/${boardId}/comment`)
}

// 댓글 작성
export const writeComment = (boardId, data) => {
  return http.post(`/v1/board/${boardId}/comment`, data)
}

// 댓글 수정
export const updateComment = (boardId, commentId, data) => {
  return http.put(`/v1/board/${boardId}/comment/${commentId}`, data)
}

// 댓글 삭제
export const deleteComment = (boardId, commentId) => {
  return http.delete(`/v1/board/${boardId}/comment/${commentId}`)
}
