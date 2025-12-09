<template>
  <div class="min-h-screen bg-background p-4 pb-24">
    <div v-if="loading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
    </div>
    
    <div v-else-if="post" class="max-w-2xl mx-auto space-y-6">
      <!-- Back Button -->
      <Button variant="ghost" class="flex items-center gap-2 -ml-2 text-primary hover:bg-primary/10" @click="$router.back()">
        <ChevronLeftIcon class="w-4 h-4" />
        {{ $t('post.back_to_board') }}
      </Button>

      <!-- Post Header -->
      <Card class="border-border">
        <CardHeader class="space-y-4">
          <div class="flex justify-between items-start gap-4">
          <h1 class="text-2xl font-bold text-foreground leading-tight flex-1">{{ post.title }}</h1>
          <!-- TODO: 로그인 구현 후 'true ||' 제거하여 권한 체크 활성화 필요 -->
          <div v-if="true || post.isAuthor" class="flex items-center gap-1 shrink-0">
             <Button variant="ghost" size="sm" class="h-8 w-8 p-0" @click="editPost">
                <EditIcon class="w-4 h-4 text-muted-foreground hover:text-foreground" />
             </Button>
             <Button variant="ghost" size="sm" class="h-8 w-8 p-0" @click="deletePostAction">
                <Trash2Icon class="w-4 h-4 text-muted-foreground hover:text-destructive" />
             </Button>
          </div>
      </div>

          <!-- Author Info -->
          <div class="flex items-center gap-3 pt-2 border-t border-border/50">
            <div class="w-10 h-10 rounded-full bg-muted flex items-center justify-center text-lg overflow-hidden">
               <!-- Placeholder Avatar or BOJ ID -->
               <span v-if="!post.userTier">👤</span>
               <span v-else>{{ post.bojId || 'U' }}</span>
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-2">
                <span class="font-semibold text-sm">{{ post.userName }}</span>
                <img v-if="post.tierNumber" :src="`https://static.solved.ac/tier_small/${post.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 inline-block" />
              </div>
              <p class="text-xs text-muted-foreground">
                {{ post.formattedDate }}
                <span v-if="post.updatedAt" class="ml-1 text-muted-foreground/70">(수정됨: {{ formatDate(post.updatedAt) }})</span>
              </p>
            </div>
          </div>
        </CardHeader>
      </Card>

      <!-- Post Content -->
      <Card class="border-border">
        <CardContent class="p-6">
          <p class="text-foreground leading-relaxed whitespace-pre-wrap">{{ post.body }}</p>
        </CardContent>
      </Card>

      <!-- Action Bar -->
      <div class="flex gap-2">
        <Button
          @click="toggleLikeAction"
          :variant="post.isLiked ? 'default' : 'outline'"
          :class="`flex-1 gap-2 rounded-lg h-11 ${post.isLiked ? 'bg-primary text-primary-foreground' : ''}`"
        >
          <ThumbsUpIcon class="w-4 h-4" />
          {{ $t('post.like') }} ({{ post.likeCount }})
        </Button>
        <Button
          @click="toggleScrapAction"
          :variant="post.isScrapped ? 'default' : 'outline'"
          :class="`flex-1 gap-2 rounded-lg h-11 ${post.isScrapped ? 'bg-primary text-primary-foreground' : ''}`"
        >
          <StarIcon :class="`w-4 h-4 ${post.isScrapped ? 'fill-current' : ''}`" />
          {{ $t('post.scrap') }}
        </Button>
      </div>

      <!-- Comment Section -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('post.comments') }} ({{ post.commentCount || 0 }})</h2>
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

          <!-- Comments List (Empty for now) -->
          <div class="space-y-4 pt-4 border-t border-border/50">
             <div v-if="comments.length === 0" class="text-center text-muted-foreground text-sm py-4">
               {{ $t('post.no_comments') || '아직 댓글이 없습니다.' }}
             </div>
             <!-- TODO: Implement Comment List when API is ready -->
          </div>
        </CardContent>
      </Card>
    </div>
    
    <div v-else class="text-center p-8 text-muted-foreground">Post not found.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getBoardDetail, toggleLike, toggleScrap, deleteBoard } from '@/api/board'
import { getTierNumber } from '@/lib/utils'
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
  Edit as EditIcon
} from 'lucide-vue-next'

const router = useRouter()
const route = useRoute()
const post = ref(null)
const loading = ref(false)
const comments = ref([]) // Placeholder

const commentText = ref('')
const anonymous = ref(false)

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  }).format(date);
}

const fetchPost = async () => {
  const boardId = route.params.id;
  if (!boardId) return;
  
  loading.value = true;
  try {
    const response = await getBoardDetail(boardId);
    if (response.data) {
       const data = response.data;
       post.value = {
         ...data,
         tierNumber: getTierNumber(data.userTier),
         formattedDate: formatDate(data.createdAt)
       };
    }
  } catch (error) {
    console.error("Failed to fetch post detail:", error);
    post.value = null;
  } finally {
    loading.value = false;
  }
}

const editPost = () => {
  if (!post.value) return;
  router.push(`/board/write?id=${post.value.boardId}`);
}

const deletePostAction = async () => {
  if (!post.value) return;
  if (!confirm('정말로 게시글을 삭제하시겠습니까?')) return;
  
  try {
    await deleteBoard(post.value.boardId);
    router.replace('/board');
  } catch (error) {
    console.error("Failed to delete post:", error);
    alert("게시글 삭제에 실패했습니다.");
  }
}

const toggleLikeAction = async () => {
  if (!post.value) return;
  try {
    const res = await toggleLike(post.value.boardId);
    // Optimistic update or refetch
    // Assuming API returns { liked: true/false }
    if (res.data && typeof res.data.liked === 'boolean') {
        post.value.isLiked = res.data.liked;
        post.value.likeCount += res.data.liked ? 1 : -1;
    }
  } catch (e) {
    console.error("Like toggle failed", e);
  }
}

const toggleScrapAction = async () => {
  if (!post.value) return;
  try {
    const res = await toggleScrap(post.value.boardId);
     if (res.data && typeof res.data.scrapped === 'boolean') {
        post.value.isScrapped = res.data.scrapped;
    }
  } catch (e) {
    console.error("Scrap toggle failed", e);
  }
}

const addComment = () => {
  console.log("Comment feature not implemented yet.");
  // TODO: Implement write comment
}

onMounted(() => {
  fetchPost();
});
</script>

<style scoped>
/* Scoped styles for Post.vue */
</style>