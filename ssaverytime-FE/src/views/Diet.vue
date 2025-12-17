<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-6xl mx-auto space-y-6">
      <!-- Header -->
      <Card class="border-border">
        <CardHeader class="flex flex-row items-center justify-between">
          <div class="flex items-center gap-3">
            <h1 class="text-xl font-bold">오늘의 식단</h1>

            <div class="flex items-center gap-2">
              <Button
                variant="outline"
                class="bg-transparent h-9 px-3"
                :disabled="pageLoading"
                @click="moveDate(-1)"
                aria-label="어제로"
              >
                ◀
              </Button>

              <span class="text-sm text-muted-foreground min-w-[110px] text-center">
                {{ selectedDate }}
              </span>

              <Button
                variant="outline"
                class="bg-transparent h-9 px-3"
                :disabled="pageLoading"
                @click="moveDate(1)"
                aria-label="내일로"
              >
                ▶
              </Button>
            </div>
          </div>

          <div class="flex items-center gap-2">
            <div v-if="pageError" class="text-sm text-red-500">
              {{ pageError }}
            </div>

            <Button
              variant="outline"
              class="bg-transparent"
              :disabled="pageLoading"
              @click="loadAll(selectedDate)"
            >
              {{ pageLoading ? '불러오는 중...' : '새로고침' }}
            </Button>
          </div>
        </CardHeader>

        <!-- ✅ 설명 문구 삭제 (요청사항) -->
      </Card>

      <!-- 내 섭취 칼로리 / 직접 먹은 음식 추가 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-3">
        <!-- Calorie Summary -->
        <Card class="border-border">
          <CardHeader class="flex flex-row items-center justify-between">
            <div class="space-y-1">
              <h2 class="font-bold text-lg">내 섭취 칼로리</h2>
            </div>

            <div class="text-xs text-muted-foreground" v-if="calorieLoading">불러오는 중...</div>
          </CardHeader>

          <CardContent class="space-y-3">
            <div v-if="calorieError" class="text-sm text-red-500">
              {{ calorieError }}
            </div>

            <div class="flex items-end justify-between rounded-lg border border-border p-4">
              <div>
                <p class="text-sm text-muted-foreground">총 섭취 칼로리</p>
                <p class="text-2xl font-bold mt-1">
                  {{ totalCaloriesDisplay }}
                </p>
              </div>
              <p class="text-xs text-muted-foreground">kcal</p>
            </div>

            <p class="text-[11px] text-muted-foreground leading-snug">
              * 날짜는 상단 ◀▶ 버튼으로 이동하며, 해당 날짜의 섭취 칼로리가 갱신됩니다.
            </p>
          </CardContent>
        </Card>

        <!-- Add Personal Intake -->
        <Card class="border-border">
          <CardHeader class="flex flex-row items-center justify-between">
            <div class="space-y-1">
              <h2 class="font-bold text-lg">직접 먹은 음식 추가</h2>
            </div>

            <Button
              variant="outline"
              class="bg-transparent"
              :disabled="addLoading"
              @click="toggleAddPanel"
            >
              {{ addPanelOpen ? '닫기' : '추가하기' }}
            </Button>
          </CardHeader>

          <CardContent class="space-y-3">
            <div v-if="addPanelOpen" class="space-y-3">
              <div class="rounded-lg border border-border p-3 space-y-2">
                <p class="text-xs text-muted-foreground">메뉴명</p>
                <input
                  v-model="addForm.menu"
                  class="w-full h-10 px-3 rounded-md border border-border bg-transparent text-sm"
                  placeholder="예) 초콜릿"
                />
              </div>

              <div class="rounded-lg border border-border p-3 space-y-2">
                <p class="text-xs text-muted-foreground">칼로리(kcal)</p>
                <input
                  v-model="addForm.calorie"
                  type="number"
                  min="0"
                  class="w-full h-10 px-3 rounded-md border border-border bg-transparent text-sm"
                  placeholder="예) 700"
                />
              </div>

              <div v-if="addError" class="text-sm text-red-500">
                {{ addError }}
              </div>

              <Button
                class="w-full"
                :disabled="addLoading"
                @click="submitPersonalDiet"
              >
                {{ addLoading ? '등록 중...' : '등록' }}
              </Button>

              <p class="text-[11px] text-muted-foreground leading-snug">
                * 등록 후, 현재 선택된 날짜({{ selectedDate }})의 섭취 칼로리를 다시 불러옵니다.
              </p>
            </div>

            <div v-else class="text-sm text-muted-foreground">
              식당 메뉴가 아닌, 개인 섭취 음식을 기록할 수 있습니다.
            </div>
          </CardContent>
        </Card>
      </div>

      <!-- Restaurants (1 row, 5 columns) -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
        <Card
          v-for="r in restaurants"
          :key="r.restaurantId"
          class="border-border"
        >
          <CardHeader class="space-y-2">
            <div class="flex items-center justify-between">
              <div class="space-y-1">
                <!-- ✅ 식당명으로 표시 -->
                <h2 class="font-bold text-base">{{ r.restaurantName }}</h2>
              </div>

              <div v-if="r.loading || r.starLoading" class="text-xs text-muted-foreground">
                로딩...
              </div>
            </div>

            <!-- 평균 별점 숫자 -->
            <div class="grid grid-cols-2 gap-2">
              <div class="rounded-lg border border-border p-2">
                <p class="text-[11px] text-muted-foreground">양 평균</p>
                <p class="text-sm font-semibold mt-1">{{ formatAvg(r.avg.AMOUNT) }}</p>
              </div>

              <div class="rounded-lg border border-border p-2">
                <p class="text-[11px] text-muted-foreground">맛 평균</p>
                <p class="text-sm font-semibold mt-1">{{ formatAvg(r.avg.TASTE) }}</p>
              </div>
            </div>

            <div v-if="r.starError" class="text-xs text-red-500">
              {{ r.starError }}
            </div>
          </CardHeader>

          <CardContent class="space-y-3">
            <!-- 메뉴 -->
            <div class="space-y-2">
              <p class="text-sm font-semibold">메뉴</p>

              <div v-if="r.error" class="text-sm text-red-500">
                {{ r.error }}
              </div>

              <div v-else-if="!r.loading && r.menus.length === 0" class="text-sm text-muted-foreground">
                메뉴 없음
              </div>

              <div v-else class="space-y-2">
                <div
                  v-for="(m, idx) in r.menus"
                  :key="`${r.restaurantId}-${idx}-${m.menu}`"
                  class="p-2 rounded-lg border border-border"
                >
                  <p class="text-sm font-medium leading-snug line-clamp-2">
                    {{ m.menu }}
                  </p>
                  <p class="text-xs text-muted-foreground mt-1">
                    {{ m.calorie }} kcal
                  </p>
                </div>
              </div>
            </div>

            <!-- 별점 주기 -->
            <div class="pt-2 border-t border-border space-y-2">
              <Button
                variant="outline"
                class="bg-transparent w-full"
                :disabled="pageLoading || r.starPosting"
                @click="toggleRatingPanel(r.restaurantId)"
              >
                {{ r.showRatingPanel ? '별점 닫기' : '별점 주기' }}
              </Button>

              <div v-if="r.showRatingPanel" class="space-y-3">
                <div class="rounded-lg border border-border p-2 space-y-2">
                  <div class="flex items-center justify-between">
                    <p class="text-sm font-semibold">양</p>
                    <p class="text-xs text-muted-foreground">1~5점</p>
                  </div>

                  <div class="flex gap-1">
                    <Button
                      v-for="s in [1,2,3,4,5]"
                      :key="`pick-amount-${r.restaurantId}-${s}`"
                      variant="outline"
                      class="bg-transparent h-8 w-8 p-0 text-xs"
                      :disabled="pageLoading || r.starPosting"
                      :class="r.pending.AMOUNT === s ? 'border-primary text-primary' : ''"
                      @click="r.pending.AMOUNT = s"
                    >
                      {{ s }}
                    </Button>
                  </div>

                  <Button
                    class="w-full"
                    :disabled="pageLoading || r.starPosting || !r.pending.AMOUNT"
                    @click="submitStar(r.restaurantId, 'AMOUNT', r.pending.AMOUNT)"
                  >
                    양 별점 등록
                  </Button>
                </div>

                <div class="rounded-lg border border-border p-2 space-y-2">
                  <div class="flex items-center justify-between">
                    <p class="text-sm font-semibold">맛</p>
                    <p class="text-xs text-muted-foreground">1~5점</p>
                  </div>

                  <div class="flex gap-1">
                    <Button
                      v-for="s in [1,2,3,4,5]"
                      :key="`pick-taste-${r.restaurantId}-${s}`"
                      variant="outline"
                      class="bg-transparent h-8 w-8 p-0 text-xs"
                      :disabled="pageLoading || r.starPosting"
                      :class="r.pending.TASTE === s ? 'border-primary text-primary' : ''"
                      @click="r.pending.TASTE = s"
                    >
                      {{ s }}
                    </Button>
                  </div>

                  <Button
                    class="w-full"
                    :disabled="pageLoading || r.starPosting || !r.pending.TASTE"
                    @click="submitStar(r.restaurantId, 'TASTE', r.pending.TASTE)"
                  >
                    맛 별점 등록
                  </Button>
                </div>

                <p class="text-[11px] text-muted-foreground leading-snug">
                  * 별점은 중복 입력 가능하며, 등록 후 평균 점수가 갱신됩니다.
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import {
  getDietByDateAndRestaurant,
  getDietStar,
  postDietStar,
  getMyCaloriesByDate,
  addPersonalDiet,
} from '@/api/diet'

/** ✅ 식당명 매핑 (요청사항) */
const RESTAURANT_NAME_MAP = {
  1: '육수고집',
  2: '소담상',
  3: '더고메',
  4: '차이나호',
  5: '속이찬새참',
}

/** 로컬 기준 YYYY-MM-DD */
const formatDateYYYYMMDD = (d = new Date()) => {
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

/** YYYY-MM-DD -> Date */
const parseYYYYMMDD = (s) => {
  const [y, m, d] = s.split('-').map(Number)
  return new Date(y, m - 1, d)
}

/** dateStr(YYYY-MM-DD)에 dayDelta 더하기 */
const addDays = (dateStr, dayDelta) => {
  const dt = parseYYYYMMDD(dateStr)
  dt.setDate(dt.getDate() + dayDelta)
  return formatDateYYYYMMDD(dt)
}

const pageLoading = ref(false)
const pageError = ref('')
const selectedDate = ref(formatDateYYYYMMDD())

/** -------------------- 섭취 칼로리 -------------------- **/
const calorieLoading = ref(false)
const calorieError = ref('')
const totalCalories = ref(null)

const extractTotalCalories = (data) => {
  if (data == null) return null
  if (typeof data === 'number') return data

  if (typeof data === 'object' && !Array.isArray(data)) {
    const candidates = ['totalCalorie', 'totalCalories', 'calorie', 'total', 'sum', 'kcal']
    for (const key of candidates) {
      if (key in data && data[key] != null && !Number.isNaN(Number(data[key]))) {
        return Number(data[key])
      }
    }
    if ('data' in data && data.data) return extractTotalCalories(data.data)
  }

  if (Array.isArray(data)) {
    const sum = data.reduce((acc, cur) => {
      const c = cur?.calorie
      if (c == null) return acc
      const n = Number(c)
      return Number.isNaN(n) ? acc : acc + n
    }, 0)
    return sum
  }

  return null
}

const totalCaloriesDisplay = computed(() => {
  if (totalCalories.value == null) return '-'
  const n = Number(totalCalories.value)
  if (Number.isNaN(n)) return '-'
  return Math.round(n).toLocaleString()
})

const loadMyCalories = async (date) => {
  calorieLoading.value = true
  calorieError.value = ''
  totalCalories.value = null

  try {
    const res = await getMyCaloriesByDate(date)
    totalCalories.value = extractTotalCalories(res?.data)
  } catch (e) {
    console.error(e)
    calorieError.value = '섭취 칼로리를 불러오지 못했습니다.'
  } finally {
    calorieLoading.value = false
  }
}

/** -------------------- 직접 섭취 추가 -------------------- **/
const addPanelOpen = ref(false)
const addLoading = ref(false)
const addError = ref('')
const addForm = ref({
  menu: '',
  calorie: '',
})

const toggleAddPanel = () => {
  addPanelOpen.value = !addPanelOpen.value
  addError.value = ''
}

const submitPersonalDiet = async () => {
  addError.value = ''
  const menu = (addForm.value.menu || '').trim()
  const calorieNum = Number(addForm.value.calorie)

  if (!menu) {
    addError.value = '메뉴명을 입력해주세요.'
    return
  }
  if (!Number.isFinite(calorieNum) || calorieNum <= 0) {
    addError.value = '칼로리는 1 이상의 숫자로 입력해주세요.'
    return
  }

  addLoading.value = true
  try {
    await addPersonalDiet({
      menu,
      calorie: Math.round(calorieNum),
    })

    addForm.value.menu = ''
    addForm.value.calorie = ''

    await loadMyCalories(selectedDate.value)
    alert('추가되었습니다.')
  } catch (e) {
    console.error(e)
    addError.value = '추가에 실패했습니다.'
  } finally {
    addLoading.value = false
  }
}

/** -------------------- 식당 메뉴/별점 -------------------- **/
const restaurants = ref(
  Array.from({ length: 5 }, (_, i) => {
    const id = i + 1
    return {
      restaurantId: id,
      restaurantName: RESTAURANT_NAME_MAP[id] ?? `식당 ${id}`,

      loading: false,
      error: '',
      menus: [],

      starLoading: false,
      starError: '',
      starPosting: false,

      avg: {
        AMOUNT: null,
        TASTE: null,
      },

      showRatingPanel: false,
      pending: {
        AMOUNT: null,
        TASTE: null,
      },
    }
  })
)

const clampScore = (n) => {
  const x = Number(n)
  if (Number.isNaN(x)) return null
  return Math.max(0, Math.min(5, x))
}

const formatAvg = (v) => {
  const x = clampScore(v)
  if (x == null) return '-'
  return x.toFixed(1)
}

const loadMenus = async (date, restaurantId) => {
  const target = restaurants.value.find((x) => x.restaurantId === restaurantId)
  if (!target) return

  target.loading = true
  target.error = ''
  target.menus = []

  try {
    const res = await getDietByDateAndRestaurant(date, restaurantId)
    target.menus = Array.isArray(res.data) ? res.data : []
  } catch (e) {
    console.error(e)
    target.error = '식단(메뉴)을 불러오지 못했습니다.'
    target.menus = []
  } finally {
    target.loading = false
  }
}

const loadStars = async (date, restaurantId) => {
  const target = restaurants.value.find((x) => x.restaurantId === restaurantId)
  if (!target) return

  target.starLoading = true
  target.starError = ''
  target.avg.AMOUNT = null
  target.avg.TASTE = null
  target.pending.AMOUNT = null
  target.pending.TASTE = null

  try {
    const [amountRes, tasteRes] = await Promise.all([
      getDietStar(date, restaurantId, 'AMOUNT'),
      getDietStar(date, restaurantId, 'TASTE'),
    ])

    target.avg.AMOUNT = clampScore(amountRes?.data?.averageScore)
    target.avg.TASTE = clampScore(tasteRes?.data?.averageScore)
  } catch (e) {
    console.error(e)
    target.starError = '평균 별점을 불러오지 못했습니다.'
  } finally {
    target.starLoading = false
  }
}

const loadOneRestaurant = async (date, restaurantId) => {
  await Promise.all([loadMenus(date, restaurantId), loadStars(date, restaurantId)])
}

const toggleRatingPanel = (restaurantId) => {
  const target = restaurants.value.find((x) => x.restaurantId === restaurantId)
  if (!target) return
  target.showRatingPanel = !target.showRatingPanel
}

const submitStar = async (restaurantId, category, score) => {
  const target = restaurants.value.find((x) => x.restaurantId === restaurantId)
  if (!target) return

  const s = Number(score)
  if (!Number.isInteger(s) || s < 1 || s > 5) {
    alert('별점은 1~5점만 가능합니다.')
    return
  }

  target.starPosting = true
  try {
    await postDietStar(selectedDate.value, restaurantId, { category, score: s })
    await loadStars(selectedDate.value, restaurantId)
    alert('별점이 등록되었습니다.')
  } catch (e) {
    console.error(e)
    alert('별점 등록에 실패했습니다.')
  } finally {
    target.starPosting = false
  }
}

/** -------------------- 전체 로드 -------------------- **/
const loadAll = async (date) => {
  pageLoading.value = true
  pageError.value = ''

  try {
    await loadMyCalories(date)
    const ids = [1, 2, 3, 4, 5]
    await Promise.all(ids.map((id) => loadOneRestaurant(date, id)))
  } catch (e) {
    console.error(e)
    pageError.value = '식단 정보를 불러오지 못했습니다.'
  } finally {
    pageLoading.value = false
  }
}

const moveDate = async (delta) => {
  selectedDate.value = addDays(selectedDate.value, delta)

  restaurants.value.forEach((r) => {
    r.showRatingPanel = false
    r.pending.AMOUNT = null
    r.pending.TASTE = null
  })
  addPanelOpen.value = false
  addError.value = ''

  await loadAll(selectedDate.value)
}

onMounted(() => {
  loadAll(selectedDate.value)
})
</script>
