import http from './http'

// 특정 날짜 + 식당별 식단 조회
// GET /api/v1/diet/{DATE}/{RESTAURANT_ID}
export const getDietByDateAndRestaurant = (date, restaurantId) => {
  return http.get(`/v1/diet/${date}/${restaurantId}`)
}

// ✅ 별점 평균 조회
// GET /api/v1/diet/{DATE}/star/{RESTAURANT_ID}/{enumtype}
// enumtype: AMOUNT | TASTE
// response example: { "averageScore": 0.0 }
export const getDietStar = (date, restaurantId, enumType) => {
  return http.get(`/v1/diet/${date}/star/${restaurantId}/${enumType}`)
}

// ✅ 별점 등록 (중복 허용)
// POST /api/v1/diet/{DATE}/star/{RESTAURANT_ID}
// body: { category: 'AMOUNT' | 'TASTE', score: number }
export const postDietStar = (date, restaurantId, payload) => {
  return http.post(`/v1/diet/${date}/star/${restaurantId}`, payload)
}
