<template>
  <div class="min-h-screen bg-background p-4">
    <div class="max-w-3xl mx-auto space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>{{ $t('board.write_post') || '게시글 작성' }}</CardTitle>
          <CardDescription>새로운 게시글을 작성합니다.</CardDescription>
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

          <!-- 공개 여부 등 추가 옵션이 필요하면 여기에 배치 -->
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

const router = useRouter()

const isSubmitting = ref(false)
const form = reactive({
  title: '',
  body: '',
  visible: '1' // 기본 공개
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
