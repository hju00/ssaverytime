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
                :disabled="loading"
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
                :disabled="loading"
                @click="moveDate(1)"
                aria-label="내일로"
              >
                ▶
              </Button>
            </div>
          </div>

          <div class="flex items-center gap-2">
            <div v-if="error" class="text-sm text-red-500">
              {{ error }}
            </div>

            <Button
              variant="outline"
              class="bg-transparent"
              :disabled="loading"
              @click="loadDiet(selectedDate)"
            >
              {{ loading ? '불러오는 중...' : '새로고침' }}
            </Button>
          </div>
        </CardHeader>

        <CardContent class="text-sm text-muted-foreground">
          식당 5곳(1~5)의 메뉴와 평균 별점(양/맛)을 불러와서 보여줍니다.
        </CardContent>
      </Card>

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
                <h2 class="font-bold text-base">식당 {{ r.restaurantId }}</h2>
                <p class="text-[10px] text-muted-foreground">
                  /diet/{{ selectedDate }}/{{ r.restaurantId }}
                </p>
              </div>

              <div v-if="r.loading || r.starLoading" class="text-xs text-muted-foreground">
                로딩...
              </div>
            </div>

            <!-- ✅ 평균 별점 숫자 표시 (별 제거) -->
            <div class="grid grid-cols-2 gap-2">
              <div class="rounded-lg border border-border p-2">
                <p class="text-[11px] text-muted-foreground">양 평균</p>
                <p class="text-sm font-semibold mt-1">
                  {{ formatAvg(r.avg.AMOUNT) }}
                </p>
              </div>

              <div class="rounded-lg border border-border p-2">
                <p class="text-[11px] text-muted-foreground">맛 평균</p>
                <p class="text-sm font-semibold mt-1">
                  {{ formatAvg(r.avg.TASTE) }}
                </p>
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
                :disabled="loading || r.starPosting"
                @click="toggleRatingPanel(r.restaurantId)"
              >
                {{ r.showRatingPanel ? '별점 닫기' : '별점 주기' }}
              </Button>

              <!-- ✅ 별점 입력 패널 (중복 허용 => 항상 가능) -->
              <div v-if="r.showRatingPanel" class="space-y-3">
                <div class="rounded-lg border border-border p-2 space-y-2">
                  <div class="flex items-center justify-between">
                    <p class="text-sm font-semibold">양(AMOUNT)</p>
                    <p class="text-xs text-muted-foreground">1~5점</p>
                  </div>

                  <div class="flex gap-1">
                    <Button
                      v-for="s in [1,2,3,4,5]"
                      :key="`pick-amount-${r.restaurantId}-${s}`"
                      variant="outline"
                      class="bg-transparent h-8 w-8 p-0 text-xs"
                      :disabled="loading || r.starPosting"
                      :class="r.pending.AMOUNT === s ? 'border-primary text-primary' : ''"
                      @click="r.pending.AMOUNT = s"
                    >
                      {{ s }}
                    </Button>
                  </div>

                  <Button
                    class="w-full"
                    :disabled="loading || r.starPosting || !r.pending.AMOUNT"
                    @click="submitStar(r.restaurantId, 'AMOUNT', r.pending.AMOUNT)"
                  >
                    양 별점 등록
                  </Button>
                </div>

                <div class="rounded-lg border border-border p-2 space-y-2">
                  <div class="flex items-center justify-between">
                    <p class="text-sm font-semibold">맛(TASTE)</p>
                    <p class="text-xs text-muted-foreground">1~5점</p>
                  </div>

                  <div class="flex gap-1">
                    <Button
                      v-for="s in [1,2,3,4,5]"
                      :key="`pick-taste-${r.restaurantId}-${s}`"
                      variant="outline"
                      class="bg-transparent h-8 w-8 p-0 text-xs"
                      :disabled="loading || r.starPosting"
                      :class="r.pending.TASTE === s ? 'border-primary text-primary' : ''"
                      @click="r.pending.TASTE = s"
                    >
                      {{ s }}
                    </Button>
                  </div>

                  <Button
                    class="w-full"
                    :disabled="loading || r.starPosting || !r.pending.TASTE"
                    @click="submitStar(r.restaurantId, 'TASTE', r.pending.TASTE)"
                  >
                    맛 별점 등록
                  </Button>
                </div>

                <p class="text-[11px] text-muted-foreground leading-snug">
                  * 중복 별점 입력이 가능합니다. 등록 후 평균 점수가 갱신됩니다.
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
import { ref, onMounted } from 'vue'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { getDietByDateAndRestaurant, getDietStar, postDietStar } from '@/api/diet'

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

const loading = ref(false)
const error = ref('')
const selectedDate = ref(formatDateYYYYMMDD())

const restaurants = ref(
  Array.from({ length: 5 }, (_, i) => ({
    restaurantId: i + 1,

    loading: false,
    error: '',
    menus: [],

    starLoading: false,
    starError: '',
    starPosting: false,

    // ✅ 평균 점수 저장
    avg: {
      AMOUNT: null,
      TASTE: null,
    },

    showRatingPanel: false,
    pending: {
      AMOUNT: null,
      TASTE: null,
    },
  }))
)

const clampScore = (n) => {
  const x = Number(n)
  if (Number.isNaN(x)) return null
  // 평균은 0~5 범위라고 가정
  return Math.max(0, Math.min(5, x))
}

// ✅ 평균 점수 표시용 (예: 0.0, 3.5)
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

  try {
    const [amountRes, tasteRes] = await Promise.all([
      getDietStar(date, restaurantId, 'AMOUNT'),
      getDietStar(date, restaurantId, 'TASTE'),
    ])

    // ✅ response: { averageScore: 0.0 }
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
  await Promise.all([
    loadMenus(date, restaurantId),
    loadStars(date, restaurantId),
  ])
}

const loadDiet = async (date) => {
  loading.value = true
  error.value = ''

  try {
    const ids = [1, 2, 3, 4, 5]
    await Promise.all(ids.map((id) => loadOneRestaurant(date, id)))
  } catch (e) {
    console.error(e)
    error.value = '식단을 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

const moveDate = async (delta) => {
  selectedDate.value = addDays(selectedDate.value, delta)

  // 날짜 이동 시 패널 닫고 선택 초기화
  restaurants.value.forEach((r) => {
    r.showRatingPanel = false
    r.pending.AMOUNT = null
    r.pending.TASTE = null
  })

  await loadDiet(selectedDate.value)
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
    await postDietStar(selectedDate.value, restaurantId, {
      category,
      score: s,
    })

    // 등록 후 평균 다시 로드
    await loadStars(selectedDate.value, restaurantId)
    alert('별점이 등록되었습니다.')
  } catch (e) {
    console.error(e)
    alert('별점 등록에 실패했습니다.')
  } finally {
    target.starPosting = false
  }
}

onMounted(() => {
  loadDiet(selectedDate.value)
})
</script>
