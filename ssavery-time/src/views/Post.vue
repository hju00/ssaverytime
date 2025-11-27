<template>
  <div class="min-h-screen bg-background p-4 pb-24">
    <div v-if="selectedPost" class="max-w-2xl mx-auto space-y-6">
      <!-- Back Button -->
      <Button variant="ghost" class="flex items-center gap-2 -ml-2 text-primary hover:bg-primary/10" @click="$router.back()">
        <ChevronLeftIcon class="w-4 h-4" />
        {{ $t('post.back_to_board') }}
      </Button>

      <!-- Post Header -->
      <Card class="border-border">
        <CardHeader class="space-y-4">
          <h1 class="text-2xl font-bold text-foreground leading-tight">{{ selectedPost.title }}</h1>

          <!-- Author Info -->
          <div class="flex items-center gap-3 pt-2 border-t border-border/50">
            <div class="w-10 h-10 rounded-full bg-muted flex items-center justify-center text-lg">
              {{ selectedPost.avatar }}
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-2">
                <span class="font-semibold text-sm">{{ selectedPost.author }}</span>
                <img v-if="selectedPost.tierNumber" :src="`https://static.solved.ac/tier_small/${selectedPost.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
              </div>
              <p class="text-xs text-muted-foreground">{{ selectedPost.date }}</p>
            </div>
          </div>
        </CardHeader>
      </Card>

      <!-- Post Content -->
      <Card class="border-border">
        <CardContent class="p-6">
          <p class="text-foreground leading-relaxed whitespace-pre-wrap">{{ selectedPost.content }}</p>
        </CardContent>
      </Card>

      <!-- Action Bar -->
      <div class="flex gap-2">
        <Button
          @click="liked = !liked"
          :variant="liked ? 'default' : 'outline'"
          :class="`flex-1 gap-2 rounded-lg h-11 ${liked ? 'bg-primary text-primary-foreground' : ''}`"
        >
          <ThumbsUpIcon class="w-4 h-4" />
          {{ $t('post.like') }} ({{ selectedPost.likes }})
        </Button>
        <Button
          @click="scraped = !scraped"
          :variant="scraped ? 'default' : 'outline'"
          :class="`flex-1 gap-2 rounded-lg h-11 ${scraped ? 'bg-primary text-primary-foreground' : ''}`"
        >
          <StarIcon :class="`w-4 h-4 ${scraped ? 'fill-current' : ''}`" />
          {{ $t('post.scrap') }}
        </Button>
      </div>

      <!-- Comment Section -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('post.comments') }} ({{ selectedPostComments.length }})</h2>
        </CardHeader>
        <CardContent class="space-y-4">
          <!-- Comment Input -->
          <div class="space-y-3 p-4 bg-muted rounded-lg">
            <Input
              :placeholder="$t('post.write_comment_placeholder')"
              v-model="commentText"
              class="bg-background border-input rounded-lg"
            />
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <Checkbox
                  id="anonymous"
                  v-model:checked="anonymous"
                />
                <label for="anonymous" class="text-sm cursor-pointer text-foreground">
                  {{ $t('post.write_anonymously') }}
                </label>
              </div>
              <Button
                class="bg-primary text-primary-foreground hover:bg-primary/90 rounded-lg h-9 px-4"
                size="sm"
                @click="addComment"
              >
                {{ $t('post.post_button') }}
              </Button>
            </div>
          </div>

          <!-- Comments List -->
          <div class="space-y-4 pt-4 border-t border-border/50">
            <div v-for="comment in selectedPostComments" :key="comment.id" class="space-y-3">
              <!-- Parent Comment -->
              <div class="flex gap-3">
                <div class="w-8 h-8 rounded-full bg-muted flex items-center justify-center text-sm flex-shrink-0">
                  👤
                </div>
                <div class="flex-1">
                  <div class="flex items-center gap-2">
                    <span class="font-semibold text-sm">{{ comment.author }}</span>
                    <img v-if="comment.tierNumber" :src="`https://static.solved.ac/tier_small/${comment.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
                  </div>
                  <p class="text-sm text-muted-foreground mt-1">{{ comment.content }}</p>
                  <div class="flex items-center gap-4 mt-2">
                    <button class="text-xs text-muted-foreground hover:text-primary flex items-center gap-1">
                      <ReplyIcon class="w-3 h-3" />
                      {{ $t('post.reply_button') }}
                    </button>
                    <span class="text-xs text-muted-foreground">{{ comment.time }}</span>
                  </div>
                </div>
                <Button variant="ghost" size="sm" class="p-2 text-destructive hover:bg-destructive/10 h-auto">
                  <Trash2Icon class="w-4 h-4" />
                </Button>
              </div>

              <!-- Child Comments -->
              <div
                v-for="child in comment.children"
                :key="child.id"
                class="ml-11 space-y-2 p-3 bg-muted/50 rounded-lg border-l-2 border-primary/30"
              >
                <div class="flex items-center gap-2">
                  <span class="font-semibold text-sm">{{ child.author }}</span>
                  <img v-if="child.tierNumber" :src="`https://static.solved.ac/tier_small/${child.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
                </div>
                <p class="text-sm text-foreground">{{ child.content }}</p>
                <span class="text-xs text-muted-foreground">{{ child.time }}</span>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
    <div v-else class="text-center p-8 text-muted-foreground">Post not found.</div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { Checkbox } from '@/components/ui/checkbox'
import {
  ThumbsUp as ThumbsUpIcon,
  Star as StarIcon,
  Reply as ReplyIcon,
  Trash2 as Trash2Icon,
  ChevronLeft as ChevronLeftIcon,
} from 'lucide-vue-next'

const route = useRoute()

const liked = ref(false)
const scraped = ref(false)
const commentText = ref('')
const anonymous = ref(false)

// --- MOCK DATA ---
const allPostsData = [
  {
    id: 1,
    title: '싸피에서 공부하기 좋은 장소 추천!',
    author: '공부벌레',
    tier: 'gold',
    tierNumber: 11, // Gold 5
    avatar: '👤',
    date: '2025년 11월 27일',
    content: `싸피 캠퍼스 내에서 공부하기 좋은 장소들을 탐험하고 저의 발견을 공유합니다. 일주일간 여러 곳을 테스트한 후 최고의 장소들을 선정했습니다.`,
    likes: 24,
    commentsCount: 8,
    postId: 1
  },
  {
    id: 2,
    title: '알고리즘을 잘하는 법??',
    author: '알고리즘 왕',
    tier: 'diamond',
    tierNumber: 31, // Master
    avatar: '👩‍💻',
    date: '2025년 11월 27일',
    content: `재능의 영역입니다. 포기하세요 ㅋ 2번째 게시물 입니다.`,
    likes: 156,
    commentsCount: 42,
    postId: 2
  },
  {
    id: 3,
    title: '근처 카페 추천 리스트',
    author: '커피중독자',
    tier: 'silver',
    tierNumber: 6, // Silver 5
    avatar: '☕',
    date: '2025년 11월 27일',
    content: `캠퍼스에서 도보 거리에 있는 최고의 카페 목록입니다. 커피 향 가득한 곳에서 휴식을 취해보세요!`,
    likes: 45,
    commentsCount: 12,
    postId: 3
  },
  {
    id: 4,
    title: '인턴십 경험 공유합니다',
    author: '취뽀성공',
    tier: 'gold',
    tierNumber: 11, // Gold 5
    avatar: '👨‍💼',
    date: '2025년 11월 27일',
    content: `최근 IT 회사에서 여름 인턴십을 마쳤습니다. 제가 배운 점들을 공유합니다. 궁금한 점은 댓글로 남겨주세요!`,
    likes: 89,
    commentsCount: 28,
    postId: 4
  },
]

const allCommentsData = [
  {
    postId: 1, // Link to post 1
    id: 1,
    author: '코드마스터',
    tier: 'diamond',
    tierNumber: 21, // Diamond 5
    content: '훌륭한 추천입니다! 특히 옥상 공간이 마음에 들어요.',
    time: '1시간 전',
    replies: 2,
    children: [],
  },
  {
    postId: 1, // Link to post 1
    id: 2,
    author: '익명',
    tier: null,
    tierNumber: 1, // Bronze 5 for anonymous users
    content: '정보 공유 감사합니다! 꼭 방문해볼게요.',
    time: '2시간 전',
    replies: 0,
    children: [],
  },
  {
    postId: 1, // Link to post 1
    id: 3,
    author: '커피사랑',
    tier: 'silver',
    tierNumber: 6, // Silver 5
    content: '엔지니어링 건물 근처 카페가 최고예요!',
    time: '3시간 전',
    replies: 1,
    children: [
      {
        id: 31,
        author: '공부벌레',
        tier: 'gold',
        tierNumber: 11, // Gold 5
        content: '동의합니다! 카푸치노가 정말 맛있어요.',
        time: '2시간 전',
      },
    ],
  },
  // Comments for Post 2
  {
    postId: 2,
    id: 101,
    author: '알고리즘 고수',
    tier: 'ruby',
    tierNumber: 26,
    content: '아주 좋은 팁입니다! 어려운 문제 해결에 도움이 되었어요.',
    time: '50분 전',
    replies: 0,
    children: []
  },
  {
    postId: 2,
    id: 102,
    author: '익명2',
    tier: null,
    tierNumber: 1, // Bronze 5 for anonymous users
    content: '재능 영역이라니... 희망이 없네요 ㅠㅠ',
    time: '30분 전',
    replies: 0,
    children: []
  },
  // Comments for Post 3
  {
    postId: 3,
    id: 201,
    author: '카페탐방',
    tier: 'gold',
    tierNumber: 11,
    content: '이 리스트 참고해서 이번 주말에 가봐야겠어요!',
    time: '1일 전',
    replies: 0,
    children: []
  },
  // Comments for Post 4
  {
    postId: 4,
    id: 301,
    author: '취준생',
    tier: 'silver',
    tierNumber: 6,
    content: '인턴십 경험 정말 부럽습니다. 어떤 기술 스택을 사용하셨나요?',
    time: '7시간 전',
    replies: 1,
    children: [
      {
        id: 302,
        author: '취뽀성공',
        tier: 'gold',
        tierNumber: 11,
        content: '주로 React와 Node.js를 사용했습니다. 백엔드는 AWS 기반이었어요.',
        time: '5시간 전',
      },
    ],
  },
]
// --- END MOCK DATA ---

const postId = computed(() => parseInt(route.params.id))
const selectedPost = computed(() => allPostsData.find(p => p.id === postId.value))

const selectedPostComments = computed(() => {
  // Filter comments based on the selected post's ID
  // For nested comments, you'd need more complex filtering or ensure children also have postId
  return allCommentsData.filter(comment => comment.postId === postId.value)
})

const tierColors = {
  gold: 'bg-yellow-100 text-yellow-800',
  silver: 'bg-gray-300 text-gray-800',
  bronze: 'bg-orange-100 text-orange-800',
  diamond: 'bg-blue-100 text-blue-800',
}

const addComment = () => {
  if (!selectedPost.value) return // Cannot add comment if no post selected
  console.log('New comment for post', selectedPost.value.id, ':', commentText.value, 'Anonymous:', anonymous.value)
  // Logic to add comment to comments array (would typically involve backend)
  commentText.value = '' // Clear input
  anonymous.value = false // Reset anonymous checkbox
}
</script>

<style scoped>
/* Scoped styles for Post.vue */
</style>