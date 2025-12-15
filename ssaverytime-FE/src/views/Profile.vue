<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-2xl mx-auto space-y-6">
      <!-- Profile Card -->
      <Card class="border-border text-center">
        <CardContent class="p-8 space-y-4">
          <div class="w-20 h-20 rounded-full flex items-center justify-center mx-auto overflow-hidden border border-border bg-muted">
             <img 
               v-if="user.tierNumber !== undefined" 
               :src="`https://static.solved.ac/tier_small/${user.tierNumber}.svg`" 
               alt="Profile" 
               class="w-16 h-16 object-contain" 
             />
             <span v-else class="text-4xl">👤</span>
          </div>
          <div>
            <h1 class="text-2xl font-bold text-foreground">{{ user.nickname }}</h1>
            <p class="text-sm text-muted-foreground">{{ user.season }}기</p>
          </div>
        </CardContent>
      </Card>

      <!-- Rank Section -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('profile.baekjoon_tier') }}</h2>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <!-- Tier Icon -->
              <div class="w-12 h-12 flex items-center justify-center">
                 <img 
                   v-if="user.tierNumber !== undefined" 
                   :src="`https://static.solved.ac/tier_small/${user.tierNumber}.svg`" 
                   alt="Tier" 
                   class="w-10 h-10 object-contain" 
                 />
              </div>
              <div>
                <p class="font-semibold text-lg">{{ user.tier || 'Unrated' }}</p>
                <p class="text-xs text-muted-foreground">{{ $t('profile.algorithm_rank') }}</p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <!-- Menu Grid -->
      <div class="grid grid-cols-2 gap-3">
        <Button
          v-for="(item, i) in menuItems"
          :key="i"
          variant="outline"
          class="h-24 flex flex-col items-start justify-between p-4 rounded-lg border-border hover:bg-muted hover:border-primary group bg-transparent"
        >
          <component :is="item.icon" class="w-6 h-6 text-primary group-hover:text-primary/80" />
          <div class="text-left">
            <p class="font-semibold text-sm text-foreground">{{ $t(item.label) }}</p>
            <p v-if="item.count !== null" class="text-xs text-muted-foreground">{{ item.count }}</p>
          </div>
        </Button>
      </div>

      <!-- Scrapped Posts Preview -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('profile.recently_scrapped') }}</h2>
        </CardHeader>
        <CardContent class="space-y-3">
          <div
            v-for="post in scrappedPosts"
            :key="post.boardId"
            @click="$router.push(`/post/${post.boardId}`)"
            class="flex items-start justify-between p-3 hover:bg-muted rounded-lg transition-colors cursor-pointer group"
          >
            <div class="flex-1">
              <h3 class="font-medium text-sm text-foreground line-clamp-2 group-hover:text-primary">
                {{ post.title }}
              </h3>
              <p class="text-xs text-muted-foreground mt-1">{{ $t('profile.by_prefix') }} {{ post.userName }}</p>
            </div>
            <ChevronRightIcon class="w-4 h-4 text-muted-foreground flex-shrink-0 mt-1" />
          </div>
          <div v-if="scrappedPosts.length === 0" class="text-center text-sm text-muted-foreground py-4">
            스크랩한 게시글이 없습니다.
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyPage } from '@/api/mypage'
import { getScrapList } from '@/api/board'
import { getTierNumber } from '@/lib/utils'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import {
  FileText as FileTextIcon,
  MessageSquare as MessageSquareIcon,
  Bookmark as BookmarkIcon,
  Settings as SettingsIcon,
  ChevronRight as ChevronRightIcon,
} from 'lucide-vue-next'

const router = useRouter()

const user = ref({
  nickname: '',
  season: '',
  tier: '',
  tierNumber: 0,
})

const menuItems = ref([
  { icon: FileTextIcon, label: 'My Posts', count: null }, // Count API needed
  { icon: MessageSquareIcon, label: 'My Comments', count: null }, // Count API needed
  { icon: BookmarkIcon, label: 'Scrapped Posts', count: null }, // Will update with length
  { icon: SettingsIcon, label: 'Edit Profile', count: null },
])

const scrappedPosts = ref([])

const fetchProfile = async () => {
  try {
    const res = await getMyPage()
    user.value = {
      nickname: res.data.name,
      season: res.data.season,
      tier: res.data.baekjoon,
      tierNumber: getTierNumber(res.data.baekjoon)
    }
  } catch (error) {
    console.error("Failed to fetch profile:", error)
    // alert("로그인이 필요합니다.")
    // router.push('/login')
  }
}

const fetchScraps = async () => {
  try {
    const res = await getScrapList({ page: 1, size: 5 }) // 최근 5개만
    scrappedPosts.value = res.data
    menuItems.value[2].count = res.data.length // Update count (simple version)
  } catch (error) {
    console.error("Failed to fetch scraps:", error)
  }
}

onMounted(() => {
  fetchProfile()
  fetchScraps()
})
</script>

<style scoped>
/* Scoped styles for Profile.vue */
</style>