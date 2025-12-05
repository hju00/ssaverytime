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
          v-for="post in posts"
          :key="post.boardId"
          :to="`/post/${post.boardId}`"
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
                <span class="text-xs text-muted-foreground whitespace-nowrap shrink-0">{{ formatDate(post.createdAt) }}</span>
              </div>

              <!-- Preview -->
              <p class="text-sm text-muted-foreground line-clamp-2">{{ post.summary }}</p>

              <!-- Metadata Row -->
              <div class="flex items-center justify-between pt-2 border-t border-border/50">
                <div class="flex items-center gap-2">
                  <span class="text-xs font-medium text-foreground">{{ post.userName }}</span>
                  <img 
                    v-if="post.tierNumber > 0"
                    :src="`https://static.solved.ac/tier_small/${post.tierNumber}.svg`" 
                    alt="Tier Icon" 
                    class="w-4 h-4 inline-block" 
                  />
                </div>

                <!-- Engagement Icons -->
                <div class="flex items-center gap-3 text-xs text-muted-foreground">
                  <div class="flex items-center gap-1" :class="{'text-red-500': post.isLiked}">
                    <HeartIcon class="w-4 h-4" :fill="post.isLiked ? 'currentColor' : 'none'" />
                    <span>{{ post.likeCount }}</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <MessageCircleIcon class="w-4 h-4" />
                    <span>{{ post.commentCount }}</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </RouterLink>
        
        <p v-if="posts.length === 0 && !isLoading" class="text-center text-muted-foreground text-sm py-8">{{ $t('board.no_meals_logged') }}</p>
        
        <!-- Loading / Infinite Scroll Trigger -->
        <div ref="loadMoreTrigger" class="py-4 text-center">
          <div v-if="isLoading" class="animate-spin rounded-full h-6 w-6 border-b-2 border-primary mx-auto"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
import { getBoardList } from '@/api/board'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
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

// State
const posts = ref([])
const sortBy = ref('latest')
const searchQuery = ref('')
const page = ref(1)
const size = ref(10) // Items per page
const isLoading = ref(false)
const hasMore = ref(true)
const loadMoreTrigger = ref(null)

// Helper: Convert Tier String to ID
const getTierNumber = (tierStr) => {
  if (!tierStr) return 0;
  const tiers = ['BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'RUBY', 'MASTER'];
  const levels = {'I': 5, 'II': 4, 'III': 3, 'IV': 2, 'V': 1};
  
  const parts = tierStr.split(' ');
  if (parts.length < 2) return 0; // e.g. "MASTER" handling separate if needed
  
  const tierIndex = tiers.indexOf(parts[0]);
  const level = levels[parts[1]] || 0;
  
  if (tierIndex === -1) return 0;
  
  // Formula: (Tier Index * 5) + Level
  // Bronze V = 1, Bronze I = 5
  // Silver V = 6 ...
  // Need to invert level logic? solved.ac: V=1, IV=2... I=5 in each tier block?
  // Actually: Bronze V = 1, Bronze I = 5. 
  // My logic: (0 * 5) + 1 = 1. Correct.
  
  // Wait, standard mapping:
  // Bronze V (1) ... Bronze I (5)
  // Silver V (6) ...
  // My level map: I=5, V=1.
  // (TierIndex * 5) + (6 - LevelNum)?
  // Let's stick to simple manual map or:
  // Level V -> 1, IV -> 2, III -> 3, II -> 4, I -> 5
  // So Bronze V = 0*5 + 1 = 1. Bronze I = 0*5 + 5 = 5.
  // Silver V = 1*5 + 1 = 6.
  
  const levelNum = {'V': 1, 'IV': 2, 'III': 3, 'II': 4, 'I': 5}[parts[1]] || 0;
  return (tierIndex * 5) + levelNum;
}

// Fetch Data
const fetchPosts = async (reset = false) => {
  if (isLoading.value) return;
  if (!hasMore.value && !reset) return;

  isLoading.value = true;

  if (reset) {
    page.value = 1;
    posts.value = [];
    hasMore.value = true;
  }

  try {
    const response = await getBoardList({
      keyword: searchQuery.value,
      sort: sortBy.value,
      page: page.value,
      size: size.value
    });

    const newPosts = response.data.map(p => ({
      ...p,
      tierNumber: getTierNumber(p.userTier),
      // Backend DTO uses camelCase: boardId, title, summary, etc.
    }));

    if (newPosts.length < size.value) {
      hasMore.value = false;
    }

    if (reset) {
      posts.value = newPosts;
    } else {
      posts.value = [...posts.value, ...newPosts];
    }
    
    page.value++;

  } catch (error) {
    console.error("Failed to fetch posts:", error);
  } finally {
    isLoading.value = false;
  }
}

// Watchers
watch([searchQuery, sortBy], () => {
  fetchPosts(true); // Reset and fetch
});

// Infinite Scroll Observer
let observer = null;

onMounted(() => {
  fetchPosts(true);

  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && hasMore.value && !isLoading.value) {
      fetchPosts(false);
    }
  }, { threshold: 0.1 });

  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
});

onUnmounted(() => {
  if (observer) observer.disconnect();
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date);
}
</script>

<style scoped>
/* Scoped styles for Board.vue */
</style>