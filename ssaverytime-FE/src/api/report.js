import http from './http'

// 게시글 신고
export const reportBoard = (boardId, reason) => {
  return http.post(`/v1/board/${boardId}/report`, { reason })
}

// 댓글 신고
export const reportComment = (boardId, commentId, reason) => {
  return http.post(`/v1/board/${boardId}/comment/${commentId}/report`, { reason })
}
