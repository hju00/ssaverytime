<template>
  <div class="min-h-screen bg-background p-4">
    <div class="max-w-3xl mx-auto space-y-6">
      <Card>
        <CardHeader class="flex flex-row items-center gap-4 space-y-0 pb-2">
          <!-- Avatar (Tier Image) -->
          <div class="h-12 w-12 rounded-full bg-muted flex items-center justify-center border shrink-0">
             <img 
               v-if="user.userTierSrc" 
               :src="user.userTierSrc" 
               alt="Tier" 
               class="h-8 w-8 object-contain" 
             />
             <span v-else class="text-xl">👤</span>
          </div>
          
          <div class="flex flex-col">
            <CardTitle class="text-lg">{{ user.name }}</CardTitle>
          </div>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="space-y-2">
            <Label for="title">제목</Label>
            <Input 
              id="title" 
              v-model="form.title" 
              placeholder="제목을 입력하세요" 
            />
          </div>
          
          <div class="space-y-2">
            <Label for="content">내용</Label>
            <Textarea 
              id="content" 
              v-model="form.body" 
              placeholder="내용을 자유롭게 작성해주세요" 
              class="min-h-[300px]"
            />
          </div>

          <!-- 익명 옵션 -->
          <div class="flex items-center space-x-2 pt-2">
            <input 
              type="checkbox" 
              id="anonymous" 
              class="h-4 w-4 rounded border-gray-300 text-primary focus:ring-primary cursor-pointer accent-primary disabled:cursor-not-allowed disabled:opacity-50"
              :disabled="isEditMode"
              :checked="form.visible === '0'" 
              @change="(e) => form.visible = e.target.checked ? '0' : '1'" 
            />
            <Label for="anonymous" class="cursor-pointer" :class="{ 'cursor-not-allowed opacity-50': isEditMode }">
              익명 {{ isEditMode ? '(수정 불가)' : '' }}
            </Label>
          </div>
        </CardContent>
        <CardFooter class="flex justify-end gap-2">
          <Button variant="outline" @click="goBack">취소</Button>
          <Button @click="submitPost" :disabled="isSubmitting">
            {{ isSubmitting ? '저장 중...' : '등록' }}
          </Button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { writeBoard, getBoardDetail, updateBoard } from '@/api/board'
import http from '@/api/http'
import { getTierImageSrc, getTierName } from '@/lib/utils'
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'

const router = useRouter()
const route = useRoute()

// 사용자 정보 초기화
const user = ref({ name: '', tier: '', userTierSrc: null })

const fetchUserInfo = async () => {
  try {
    const res = await http.get('/v1/mypage')
    user.value = {
      name: res.data.name,
      tier: getTierName(res.data.baekjoon),
      userTierSrc: getTierImageSrc(res.data.baekjoon)
    }
  } catch (error) {
    console.error("Failed to fetch user info:", error)
    alert("로그인이 필요합니다.")
    router.push('/login')
  }
}

const isSubmitting = ref(false)
const isEditMode = ref(false)
const form = reactive({
  boardId: null,
  title: '',
  body: '',
  visible: '1' // 기본 공개 ('1': 실명, '0': 익명)
})

onMounted(async () => {
  await fetchUserInfo()

  const boardId = route.query.id
  if (boardId) {
    isEditMode.value = true
    form.boardId = boardId
    try {
      const response = await getBoardDetail(boardId)
      const data = response.data
      form.title = data.title
      form.body = data.body
      form.visible = data.visible
    } catch (error) {
      console.error('Failed to load post data:', error)
      alert('게시글 정보를 불러오는데 실패했습니다.')
      router.push('/board')
    }
  }
})

const goBack = () => {
  router.back()
}

const submitPost = async () => {
  if (!form.title.trim()) {
    alert('제목을 입력해주세요.')
    return
  }
  if (!form.body.trim()) {
    alert('내용을 입력해주세요.')
    return
  }

  isSubmitting.value = true
  try {
    if (isEditMode.value) {
      await updateBoard({
        boardId: form.boardId,
        title: form.title,
        body: form.body,
        visible: form.visible
      })
      alert('게시글이 수정되었습니다.')
    } else {
      await writeBoard({
        title: form.title,
        body: form.body,
        visible: form.visible
      })
    }
    
    // 성공 시 목록으로 이동
    router.push('/board')
    
  } catch (error) {
    console.error('Failed to save post:', error)
    alert('게시글 저장에 실패했습니다.')
  } finally {
    isSubmitting.value = false
  }
}
</script>
