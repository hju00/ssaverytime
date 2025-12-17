import http from './http'

// 특정 날짜 + 식당별 식단 조회
// GET /api/v1/diet/{DATE}/{RESTAURANT_ID}
export const getDietByDateAndRestaurant = (date, restaurantId) => {
  return http.get(`/v1/diet/${date}/${restaurantId}`)
}

// 별점 평균 조회
// GET /api/v1/diet/{DATE}/star/{RESTAURANT_ID}/{enumtype}
// response: { averageScore: 0.0 }
export const getDietStar = (date, restaurantId, enumType) => {
  return http.get(`/v1/diet/${date}/star/${restaurantId}/${enumType}`)
}

// 별점 등록(중복 허용)
// POST /api/v1/diet/{DATE}/star/{RESTAURANT_ID}
export const postDietStar = (date, restaurantId, payload) => {
  return http.post(`/v1/diet/${date}/star/${restaurantId}`, payload)
}

// 날짜별 섭취 칼로리 조회
// GET /api/v1/diet/{DATE}
export const getMyCaloriesByDate = (date) => {
  return http.get(`/v1/diet/${date}`)
}

// ✅ 날짜별 섭취 음식 전체 리스트 조회
// GET /api/v1/diet/{DATE}/list
// response: [{ menu, calorie }, ...]
export const getMyDietListByDate = (date) => {
  return http.get(`/v1/diet/${date}/list`)
}

// 직접 섭취한 음식 추가
// POST /api/v1/diet
export const addPersonalDiet = (payload) => {
  return http.post(`/v1/diet`, payload)
}
