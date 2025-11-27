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
          <Button
            @click="filterHot = !filterHot"
            :variant="filterHot ? 'default' : 'outline'"
            :class="`rounded-lg h-10 ${filterHot ? 'bg-primary text-primary-foreground' : ''}`"
          >
            {{ filterHot ? $t('board.hot_posts') : $t('board.all_posts') }}
          </Button>
        </div>
      </div>

      <!-- Posts List -->
      <div class="space-y-3">
                <RouterLink
                  v-for="post in filteredPosts"
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
                      <h2 class="font-bold text-lg text-foreground line-clamp-2">{{ post.title }}</h2>
        
                      <!-- Preview -->
                      <p class="text-sm text-muted-foreground line-clamp-2">{{ post.preview }}</p>
        
                      <!-- Metadata Row -->
                      <div class="flex items-center justify-between pt-2 border-t border-border/50">
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-medium text-foreground">{{ post.author }}</span>
                          <img :src="`https://static.solved.ac/tier_small/${post.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
                        </div>
        
                        <!-- Engagement Icons -->
                        <div class="flex items-center gap-1 text-xs text-muted-foreground">
                          <HeartIcon class="w-4 h-4" />
                          <span>{{ post.likes }}</span>
                          <MessageCircleIcon class="w-4 h-4 ml-2" />
                          <span>{{ post.commentsCount }}</span>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </RouterLink>        <p v-if="filteredPosts.length === 0" class="text-center text-muted-foreground text-sm py-8">{{ $t('board.no_meals_logged') }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Heart as HeartIcon, MessageCircle as MessageCircleIcon, Search as SearchIcon, Plus as PlusIcon } from 'lucide-vue-next'

const filterHot = ref(false)
const searchQuery = ref('')

const tierColors = {
  gold: 'bg-yellow-100 text-yellow-800',
  silver: 'bg-gray-300 text-gray-800',
  bronze: 'bg-orange-100 text-orange-800',
  diamond: 'bg-blue-100 text-blue-800',
}

const posts = ref([
  {
    id: 1,
    title: '싸피에서 공부하기 좋은 장소 추천!',
    preview: '싸피 캠퍼스 내에서 공부하기 좋은 장소들을 탐험하고 저의 발견을 공유합니다. 일주일간 여러 곳을 테스트한 후 최고의 장소들을 선정했습니다.',
    author: '공부벌레',
    tier: 'gold',
    tierNumber: 11, // Gold 5
    time: '2시간 전',
    likes: 24,
    commentsCount: 8,
    hot: true,
  },
  {
    id: 2,
    title: '알고리즘을 잘하는 법??',
    preview: '재능의 영역입니다. 포기하세요 ㅋ 2번째 게시물 입니다.',
    author: '알고리즘 왕',
    tier: 'diamond',
    tierNumber: 31, // Master
    time: '4시간 전',
    likes: 156,
    commentsCount: 42,
    hot: true,
  },
  {
    id: 3,
    title: '근처 카페 추천 리스트',
    preview: '캠퍼스에서 도보 거리에 있는 최고의 카페 목록입니다. 커피 향 가득한 곳에서 휴식을 취해보세요!',
    author: '커피중독자',
    tier: 'silver',
    tierNumber: 6, // Silver 5
    time: '6시간 전',
    likes: 45,
    commentsCount: 12,
    hot: false,
  },
  {
    id: 4,
    title: '인턴십 경험 공유합니다',
    preview: '최근 IT 회사에서 여름 인턴십을 마쳤습니다. 제가 배운 점들을 공유합니다. 궁금한 점은 댓글로 남겨주세요!',
    author: '취뽀성공',
    tier: 'gold',
    tierNumber: 11, // Gold 5
    time: '1일 전',
    likes: 89,
    commentsCount: 28,
    hot: true,
  },
])

const filteredPosts = computed(() => {
  let currentPosts = filterHot.value ? posts.value.filter((p) => p.hot) : posts.value
  if (searchQuery.value) {
    currentPosts = currentPosts.filter(post =>
      post.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      post.preview.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      post.author.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }
  return currentPosts
})
</script>

<style scoped>
/* Scoped styles for Board.vue */
</style>