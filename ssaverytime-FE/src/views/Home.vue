<template>
  <div class="p-6 space-y-6 bg-muted/20 min-h-screen">
    <!-- Header Section -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <!-- ✅ 로그인: "환영합니다, OOO님!" / 로그아웃: "환영합니다." -->
        <h1 class="text-3xl font-bold tracking-tight">
          <template v-if="isLoggedIn">
            환영합니다, {{ user.nickname || '싸피인' }}님!
          </template>
          <template v-else>
            환영합니다.
          </template>
        </h1>
        <p class="text-muted-foreground">오늘의 싸피 소식을 확인해보세요.</p>
      </div>
      <div class="flex items-center gap-2">
        <Button variant="outline" @click="$router.push('/profile')">
          {{ $t('sidebar.profile') }}
        </Button>
      </div>
    </div>

    <!-- ✅ Big Total Calories Card ONLY -->
    <div class="grid gap-4">
      <Card class="bg-primary text-primary-foreground">
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-base font-medium">총 섭취 칼로리</CardTitle>
          <UtensilsIcon class="h-5 w-5 opacity-70" />
        </CardHeader>
        <CardContent class="py-10">
          <div class="text-5xl md:text-6xl font-extrabold tracking-tight">
            {{ totalCaloriesText }}
          </div>
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
            <div v-if="loadingHot" class="flex justify-center py-4">
              <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-primary"></div>
            </div>

            <div
              v-else-if="hotPosts.length > 0"
              v-for="post in hotPosts"
              :key="post.boardId"
              class="flex flex-col gap-1 pb-4 border-b last:border-0 cursor-pointer hover:bg-muted/50 p-2 rounded-md transition-colors"
              @click="$router.push(`/post/${post.boardId}`)"
            >
              <div class="flex justify-between items-start">
                <h4 class="font-medium text-sm line-clamp-1">{{ post.title }}</h4>
                <!-- <Badge variant="secondary" class="text-xs">인기</Badge> -->
              </div>
              <p class="text-xs text-muted-foreground line-clamp-1">
                {{ post.summary || post.body }}
              </p>
              <div class="flex gap-3 text-xs text-muted-foreground mt-1">
                <span class="flex items-center gap-1"><HeartIcon class="w-3 h-3" /> {{ post.likeCount || 0 }}</span>
                <span class="flex items-center gap-1"><MessageSquareIcon class="w-3 h-3" /> {{ post.commentCount || 0 }}</span>
              </div>
            </div>

            <div v-else class="text-center text-sm text-muted-foreground py-4">
              인기 게시글이 없습니다.
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
  Heart as HeartIcon,
  MessageSquare as MessageSquareIcon,
} from 'lucide-vue-next'

// ✅ Diet API (이미 만든 파일 사용)
import { getMyCaloriesByDate, getMyDietListByDate } from '@/api/diet'

// ✅ Profile API (프로필에서 쓰는 것과 동일하게 이름 가져오기)
import { getMyPage } from '@/api/mypage'
import { getBoardList } from '@/api/board'

/** 오늘 날짜 YYYY-MM-DD */
const formatDateYYYYMMDD = (d = new Date()) => {
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const today = formatDateYYYYMMDD()

/** ✅ 로그인 사용자 (이름 표시용) */
const user = ref({
  nickname: '',
})

/** ✅ 로그인 여부 */
const isLoggedIn = ref(false)

const fetchMe = async () => {
  try {
    const res = await getMyPage()
    user.value.nickname = res?.data?.name ?? ''
    // ✅ getMyPage 성공 = 로그인 상태로 간주
    isLoggedIn.value = true
  } catch (e) {
    console.error(e)
    user.value.nickname = ''
    // ✅ 실패(401 등) = 로그아웃 상태로 간주
    isLoggedIn.value = false
  }
}

/** 총 섭취 칼로리 */
const totalCalories = ref(null)
const loadingCalories = ref(false)

/** 오늘 섭취 음식 목록 */
const dietList = ref([])
const loadingDiet = ref(false)

/** 인기 게시글 목록 */
const hotPosts = ref([])
const loadingHot = ref(false)

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

const fetchHotPosts = async () => {
  loadingHot.value = true
  try {
    const res = await getBoardList({ sort: 'likes', page: 1, size: 3 })
    hotPosts.value = Array.isArray(res?.data) ? res.data : []
  } catch (e) {
    console.error(e)
    hotPosts.value = []
  } finally {
    loadingHot.value = false
  }
}

onMounted(async () => {
  await Promise.all([fetchMe(), loadTodayCalories(), loadTodayDietList(), fetchHotPosts()])
})
</script>

<style scoped></style>
