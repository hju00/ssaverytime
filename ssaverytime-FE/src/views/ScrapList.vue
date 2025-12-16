<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-3xl mx-auto space-y-4">
      <h1 class="text-xl font-bold">스크랩한 게시글</h1>

      <Card
        v-for="post in scraps"
        :key="post.boardId"
        class="border-border cursor-pointer hover:bg-muted"
        @click="$router.push(`/post/${post.boardId}`)"
      >
        <CardContent class="p-4 space-y-2">
          <h2 class="font-semibold text-foreground line-clamp-2">
            {{ post.title }}
          </h2>
          <p class="text-sm text-muted-foreground">
            {{ post.userName }} · {{ post.createdAt }}
          </p>
        </CardContent>
      </Card>

      <div v-if="scraps.length === 0" class="text-center text-muted-foreground py-8">
        스크랩한 게시글이 없습니다.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getScrapList } from '@/api/board'
import { Card, CardContent } from '@/components/ui/card'

const scraps = ref([])

const fetchScraps = async () => {
  try {
    const res = await getScrapList({ page: 1, size: 20 })
    scraps.value = res.data
  } catch (e) {
    console.error(e)
    alert('스크랩 목록을 불러오지 못했습니다.')
  }
}

onMounted(() => {
  fetchScraps()
})
</script>
