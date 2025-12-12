import http from './http'

// 신고된 게시글 목록 조회
export const getReportedBoards = () => {
  return http.get('/v1/admin/board')
}

// 신고된 댓글 목록 조회
export const getReportedComments = () => {
  return http.get('/v1/admin/comment')
}

// 사용자 비활성화
export const deactivateUser = (userId) => {
  return http.put(`/v1/admin/user/${userId}`)
}

// 게시글 비활성화
export const deactivateBoard = (boardId) => {
  return http.put(`/v1/admin/board/${boardId}`)
}

// 댓글 비활성화
export const deactivateComment = (commentId) => {
  return http.put(`/v1/admin/comment/${commentId}`)
}
