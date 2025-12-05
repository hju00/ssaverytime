<template>
  <div class="min-h-screen bg-background p-4">
    <div class="max-w-3xl mx-auto space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>{{ user.name }}</CardTitle>
          <CardDescription class="flex items-center">
            <img :src="`https://static.solved.ac/tier_small/${user.tierNumber}.svg`" alt="Tier Icon" class="w-4 h-4 mr-1" />
            {{ user.tier }}
          </CardDescription>
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
            <Checkbox 
              id="anonymous" 
              :checked="form.visible === '0'" 
              @update:checked="(checked) => form.visible = checked ? '0' : '1'" 
            />
            <Label for="anonymous" class="cursor-pointer">익명</Label>
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
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { writeBoard } from '@/api/board'
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Checkbox } from '@/components/ui/checkbox'

const router = useRouter()

// TODO: 나중에 JWT 토큰에서 사용자 정보를 파싱하는 로직으로 대체 필요
const getUserInfo = () => {
  // const token = localStorage.getItem('accessToken')
  // if (!token) return null
  // return parseJwt(token)
  
  return {
    name: '유저왕(JWT)',
    tier: 'PLATINUM I',
    tierNumber: 16
  }
}

// 사용자 정보 초기화
const user = ref(getUserInfo())

const isSubmitting = ref(false)
const form = reactive({
  title: '',
  body: '',
  visible: '1' // 기본 공개 ('1': 실명, '0': 익명)
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
    // DTO 구조에 맞게 데이터 전송
    // BoardRequestDto: { title, body, visible, ... }
    await writeBoard({
      title: form.title,
      body: form.body,
      visible: form.visible
    })
    
    // 성공 시 목록으로 이동
    router.push('/board')
    
  } catch (error) {
    console.error('Failed to write post:', error)
    alert('게시글 작성에 실패했습니다.')
  } finally {
    isSubmitting.value = false
  }
}
</script>
