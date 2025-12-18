<template>
  <div class="p-6 space-y-6 bg-muted/20 min-h-screen">
    <!-- Header Section -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <h1 class="text-3xl font-bold tracking-tight">환영합니다, 싸피인님!</h1>
        <p class="text-muted-foreground">오늘의 싸피 소식을 확인해보세요.</p>
      </div>
      <div class="flex items-center gap-2">
        <Button variant="outline" @click="$router.push('/profile')">
          {{ $t('sidebar.profile') }}
        </Button>
      </div>
    </div>

    <!-- Quick Stats / Overview Cards -->
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      <Card class="bg-primary text-primary-foreground">
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">총 섭취 칼로리</CardTitle>
          <UtensilsIcon class="h-4 w-4 opacity-70" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">{{ totalCaloriesText }}</div>
          <p class="text-xs opacity-70">어제보다 +15% 증가</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">읽지 않은 공지</CardTitle>
          <BellIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">3</div>
          <p class="text-xs text-muted-foreground">캠퍼스 공지 2건, 일반 1건</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">백준 스트릭</CardTitle>
          <TrophyIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">12일째</div>
          <p class="text-xs text-muted-foreground">꾸준함이 실력입니다!</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">마일리지</CardTitle>
          <StarIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">150 pts</div>
          <p class="text-xs text-muted-foreground">등급: Gold III</p>
        </CardContent>
      </Card>
    </div>

    <!-- Main Content Area -->
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
      <!-- Left Column: Today's Diet -->
      <Card class="col-span-4 lg:col-span-4">
        <CardHeader>
          <CardTitle>{{ $t('diet.title') }}</CardTitle>
          <CardDescription>{{ $t('diet.description') }}</CardDescription>
        </CardHeader>
        <CardContent class="pl-2">
          <!-- Diet Preview -->
          <div class="space-y-4 px-4">
            <div
              v-for="(item, idx) in dietList"
              :key="`${idx}-${item.menu}`"
              class="flex items-center p-4 border rounded-lg bg-background shadow-sm"
            >
              <div class="w-12 h-12 rounded-full bg-green-100 flex items-center justify-center text-green-600 mr-4">
                <UtensilsIcon class="w-6 h-6" />
              </div>
              <div class="flex-1">
                <h4 class="font-semibold">{{ item.menu }}</h4>
                <p class="text-sm text-muted-foreground">오늘 섭취한 음식</p>
              </div>
              <Badge>{{ item.calorie }} kcal</Badge>
            </div>

            <div v-if="!loadingDiet && dietList.length === 0" class="text-sm text-muted-foreground px-1">
              오늘 섭취 기록이 없습니다.
            </div>
          </div>

          <!-- ✅ 싸피 점심 불러오기 버튼 제거 (요구사항) -->
        </CardContent>
      </Card>

      <!-- Right Column: Recent Posts (Hot Posts) -->
      <Card class="col-span-3 lg:col-span-3">
        <CardHeader>
          <CardTitle>{{ $t('board.hot_posts') }}</CardTitle>
          <CardDescription>광주 캠퍼스 인기글</CardDescription>
        </CardHeader>
        <CardContent>
          <div class="space-y-4">
            <!-- Mock Post Items -->
            <div
              v-for="i in 3"
              :key="i"
              class="flex flex-col gap-1 pb-4 border-b last:border-0 cursor-pointer hover:bg-muted/50 p-2 rounded-md transition-colors"
              @click="$router.push('/board')"
            >
              <div class="flex justify-between items-start">
                <h4 class="font-medium text-sm">알고리즘 스터디원 모집합니다!</h4>
                <Badge variant="secondary" class="text-xs">스터디</Badge>
              </div>
              <p class="text-xs text-muted-foreground line-clamp-1">
                오전 알고리즘 스터디 부원 2명 충원합니다. 백준 골드 이상...
              </p>
              <div class="flex gap-3 text-xs text-muted-foreground mt-1">
                <span class="flex items-center gap-1"><HeartIcon class="w-3 h-3" /> 12</span>
                <span class="flex items-center gap-1"><MessageSquareIcon class="w-3 h-3" /> 5</span>
              </div>
            </div>
          </div>

          <div class="mt-4">
            <Button class="w-full" @click="$router.push('/board')">
              전체 게시글 보기
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
  Utensils as UtensilsIcon,
  Bell as BellIcon,
  Trophy as TrophyIcon,
  Star as StarIcon,
  Coffee as CoffeeIcon,
  Heart as HeartIcon,
  MessageSquare as MessageSquareIcon
} from 'lucide-vue-next'

// ✅ Diet API (이미 만든 파일 사용)
import { getMyCaloriesByDate, getMyDietListByDate } from '@/api/diet'

/** 오늘 날짜 YYYY-MM-DD */
const formatDateYYYYMMDD = (d = new Date()) => {
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const today = formatDateYYYYMMDD()

/** 총 섭취 칼로리 */
const totalCalories = ref(null)
const loadingCalories = ref(false)

/** 오늘 섭취 음식 목록 */
const dietList = ref([])
const loadingDiet = ref(false)

/** 백엔드 응답 형태가 숫자/객체/배열 어떤 형태든 최대한 안전하게 합산 */
const extractTotalCalories = (data) => {
  if (data == null) return null
  if (typeof data === 'number') return data

  if (Array.isArray(data)) {
    return data.reduce((acc, cur) => {
      const n = Number(cur?.calorie)
      return Number.isNaN(n) ? acc : acc + n
    }, 0)
  }

  if (typeof data === 'object') {
    const candidates = ['totalCalorie', 'totalCalories', 'calorie', 'total', 'sum', 'kcal']
    for (const key of candidates) {
      if (key in data && data[key] != null && !Number.isNaN(Number(data[key]))) {
        return Number(data[key])
      }
    }
  }

  return null
}

const totalCaloriesText = computed(() => {
  if (loadingCalories.value) return '...'
  if (totalCalories.value == null) return '- kcal'
  const n = Number(totalCalories.value)
  if (Number.isNaN(n)) return '- kcal'
  return `${Math.round(n).toLocaleString()} kcal`
})

const loadTodayCalories = async () => {
  loadingCalories.value = true
  try {
    const res = await getMyCaloriesByDate(today)
    totalCalories.value = extractTotalCalories(res?.data)
  } catch (e) {
    console.error(e)
    totalCalories.value = null
  } finally {
    loadingCalories.value = false
  }
}

const loadTodayDietList = async () => {
  loadingDiet.value = true
  try {
    const res = await getMyDietListByDate(today)
    dietList.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    console.error(e)
    dietList.value = []
  } finally {
    loadingDiet.value = false
  }
}

onMounted(async () => {
  await Promise.all([loadTodayCalories(), loadTodayDietList()])
})
</script>

<style scoped>
</style>
