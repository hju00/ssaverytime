<template>
  <div class="min-h-screen bg-background p-4">
    <div class="max-w-3xl mx-auto space-y-6">
      <!-- Header -->
      <div class="flex flex-col gap-4">
        <div class="flex items-center justify-between">
          <h1 class="text-3xl font-bold text-foreground">{{ $t('board.title') }}</h1>
          <Button class="gap-2 bg-primary text-primary-foreground hover:bg-primary/90 rounded-lg">
            <PlusIcon class="w-4 h-4" />
            <span class="hidden sm:inline">{{ $t('board.write_post') }}</span>
          </Button>
        </div>

        <!-- Search and Filter -->
        <div class="flex gap-2 flex-col sm:flex-row">
          <div class="relative flex-1">
            <SearchIcon class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <Input
              :placeholder="$t('board.search_placeholder')"
              v-model="searchQuery"
              class="pl-10 rounded-lg bg-background border-input h-10"
            />
          </div>
          
          <Select v-model="sortBy">
            <SelectTrigger class="w-[140px] rounded-lg border-input h-10 bg-background">
              <SelectValue placeholder="정렬" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="latest">최신순</SelectItem>
              <SelectItem value="likes">인기순</SelectItem>
              <SelectItem value="comments">댓글순</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      <!-- Posts List -->
      <div class="space-y-3">
        <RouterLink
          v-for="post in visiblePosts"
          :key="post.id"
          :to="`/post/${post.id}`"
          custom
          v-slot="{ navigate }"
        >
          <Card
            @click="navigate"
            class="border-border hover:shadow-md transition-shadow cursor-pointer"
          >
            <CardContent class="p-4 space-y-3">
              <!-- Title -->
              <div class="flex justify-between items-start gap-2">
                <h2 class="font-bold text-lg text-foreground line-clamp-1">{{ post.title }}</h2>
                <span class="text-xs text-muted-foreground whitespace-nowrap shrink-0">{{ formatDate(post.time) }}</span>
              </div>

              <!-- Preview -->
              <p class="text-sm text-muted-foreground line-clamp-2">{{ post.preview }}</p>

              <!-- Metadata Row -->
              <div class="flex items-center justify-between pt-2 border-t border-border/50">
                <div class="flex items-center gap-2">
                  <span class="text-xs font-medium text-foreground">{{ post.author }}</span>
                  <img :src="`https://static.solved.ac/tier_small/${post.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
                </div>

                <!-- Engagement Icons -->
                <div class="flex items-center gap-3 text-xs text-muted-foreground">
                  <div class="flex items-center gap-1">
                    <HeartIcon class="w-4 h-4" />
                    <span>{{ post.likes }}</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <MessageCircleIcon class="w-4 h-4" />
                    <span>{{ post.commentsCount }}</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </RouterLink>
        
        <p v-if="filteredPosts.length === 0" class="text-center text-muted-foreground text-sm py-8">{{ $t('board.no_meals_logged') }}</p>
        
        <!-- Infinite Scroll Sentinel -->
        <div ref="loadMoreTrigger" class="py-4 text-center" v-if="visibleLimit < filteredPosts.length">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-primary mx-auto"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { 
  Heart as HeartIcon, 
  MessageCircle as MessageCircleIcon, 
  Search as SearchIcon, 
  Plus as PlusIcon
} from 'lucide-vue-next'

const sortBy = ref('latest')
const searchQuery = ref('')
const visibleLimit = ref(10)
const loadMoreTrigger = ref(null)
const itemsPerLoad = 5

const tierColors = {
  gold: 'bg-yellow-100 text-yellow-800',
  silver: 'bg-gray-300 text-gray-800',
  bronze: 'bg-orange-100 text-orange-800',
  diamond: 'bg-blue-100 text-blue-800',
}

// Generate more mock data for infinite scroll demonstration
const generateMockPosts = () => {
  const basePosts = [
    { title: '싸피에서 공부하기 좋은 장소 추천!', preview: '싸피 캠퍼스 내에서 공부하기 좋은 장소들을 탐험하고...', author: '공부벌레', tierNumber: 11, likes: 24, commentsCount: 8 },
    { title: '알고리즘을 잘하는 법??', preview: '재능의 영역입니다. 포기하세요 ㅋ', author: '알고리즘 왕', tierNumber: 31, likes: 156, commentsCount: 42 },
    { title: '근처 카페 추천 리스트', preview: '캠퍼스에서 도보 거리에 있는 최고의 카페 목록입니다.', author: '커피중독자', tierNumber: 6, likes: 45, commentsCount: 12 },
    { title: '인턴십 경험 공유합니다', preview: '최근 IT 회사에서 여름 인턴십을 마쳤습니다.', author: '취뽀성공', tierNumber: 11, likes: 89, commentsCount: 28 },
    { title: 'Spring Boot 질문있습니다.', preview: 'JPA 사용할 때 N+1 문제 해결하는 가장 좋은 방법이 뭔가요?', author: '자바꿈나무', tierNumber: 1, likes: 5, commentsCount: 3 },
    { title: '오늘 점심 메뉴 뭔가요?', preview: 'A코너랑 B코너 중에 뭐가 더 맛있을까요?', author: '배고픈싸피인', tierNumber: 8, likes: 12, commentsCount: 15 },
  ]

  const result = []
  for (let i = 0; i < 30; i++) {
    const base = basePosts[i % basePosts.length]
    result.push({
      id: i + 1,
      title: `${base.title} ${Math.floor(i / 6) > 0 ? `#${Math.floor(i / 6) + 1}` : ''}`,
      preview: base.preview,
      author: base.author,
      tierNumber: base.tierNumber,
      time: new Date(Date.now() - i * 3600000 * 2).toISOString(), // Decreasing time
      likes: base.likes + (i * 2),
      commentsCount: base.commentsCount + i,
    })
  }
  return result
}

const posts = ref(generateMockPosts())

// Reset infinite scroll when search or sort changes
watch([searchQuery, sortBy], () => {
  visibleLimit.value = 10
  window.scrollTo(0, 0)
})

const filteredPosts = computed(() => {
  // 1. Filter first (search)
  let currentPosts = [...posts.value]
  if (searchQuery.value) {
    currentPosts = currentPosts.filter(post =>
      post.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      post.preview.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      post.author.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  // 2. Sort
  currentPosts.sort((a, b) => {
    if (sortBy.value === 'latest') {
        return new Date(b.time) - new Date(a.time)
    } else if (sortBy.value === 'likes') {
      return b.likes - a.likes
    } else if (sortBy.value === 'comments') {
      return b.commentsCount - a.commentsCount
    }
    return 0
  })

  return currentPosts
})

const visiblePosts = computed(() => {
  return filteredPosts.value.slice(0, visibleLimit.value)
})

let observer = null

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting) {
      // Simulate network delay for better UX feeling
      setTimeout(() => {
        visibleLimit.value += itemsPerLoad
      }, 500)
    }
  }, {
    root: null,
    threshold: 0.1
  })

  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value)
  }
})

// Re-observe when the trigger element might be re-rendered (e.g. after filter changes)
watch(loadMoreTrigger, (newVal) => {
  if (newVal && observer) {
    observer.disconnect()
    observer.observe(newVal)
  }
})

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
  }
})

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date)
}
</script>

<style scoped>
/* Scoped styles for Board.vue */
</style>