<template>
  <div class="container mx-auto p-6 space-y-6">
    <div class="flex items-center justify-between">
      <h1 class="text-3xl font-bold tracking-tight">관리자 페이지</h1>
    </div>

    <Tabs default-value="board" class="w-full">
      <TabsList>
        <TabsTrigger value="board">신고된 게시글</TabsTrigger>
        <TabsTrigger value="comment">신고된 댓글</TabsTrigger>
      </TabsList>

      <!-- 게시글 탭 -->
      <TabsContent value="board" class="space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>신고 게시글 목록</CardTitle>
            <CardDescription>신고 누적 건수가 있는 게시글 목록입니다.</CardDescription>
          </CardHeader>
          <CardContent>
            <div class="rounded-md border">
              <table class="w-full caption-bottom text-sm">
                <thead class="[&_tr]:border-b">
                  <tr class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[80px]">ID</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground">제목</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground">작성자</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[100px]">신고수</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[100px]">상태</th>
                    <th class="h-12 px-4 text-right align-middle font-medium text-muted-foreground">관리</th>
                  </tr>
                </thead>
                <tbody class="[&_tr:last-child]:border-0">
                  <tr v-for="board in boards" :key="board.boardId" class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                    <td class="p-4 align-middle">{{ board.boardId }}</td>
                    <td class="p-4 align-middle font-medium">{{ board.title }}</td>
                    <td class="p-4 align-middle">
                      {{ board.userName }}
                      <span class="text-xs text-muted-foreground">({{ board.bojId }})</span>
                    </td>
                    <td class="p-4 align-middle">
                      <Badge variant="destructive">{{ board.warningCnt }}</Badge>
                    </td>
                    <td class="p-4 align-middle">
                      <Badge :variant="board.visible === '1' ? 'outline' : 'secondary'">
                        {{ board.visible === '1' ? '공개' : '숨김' }}
                      </Badge>
                    </td>
                    <td class="p-4 align-middle text-right space-x-2">
                      <Button 
                        variant="ghost" 
                        size="sm"
                        @click="handleDeactivateBoard(board.boardId)"
                        :disabled="board.visible === '0'"
                      >
                        비활성화
                      </Button>
                      <Button 
                        variant="ghost" 
                        size="sm"
                        class="text-red-500 hover:text-red-600"
                        @click="handleDeactivateUser(board.bojId)" 
                      >
                        유저 정지
                      </Button>
                    </td>
                  </tr>
                  <tr v-if="boards.length === 0">
                    <td colspan="6" class="p-4 align-middle h-24 text-center">
                      신고된 게시글이 없습니다.
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </TabsContent>

      <!-- 댓글 탭 -->
      <TabsContent value="comment" class="space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>신고 댓글 목록</CardTitle>
            <CardDescription>신고 누적 건수가 있는 댓글 목록입니다.</CardDescription>
          </CardHeader>
          <CardContent>
            <div class="rounded-md border">
              <table class="w-full caption-bottom text-sm">
                <thead class="[&_tr]:border-b">
                  <tr class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[80px]">ID</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground">내용</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground">게시글ID</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground">작성자</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[100px]">신고수</th>
                    <th class="h-12 px-4 text-left align-middle font-medium text-muted-foreground w-[100px]">상태</th>
                    <th class="h-12 px-4 text-right align-middle font-medium text-muted-foreground">관리</th>
                  </tr>
                </thead>
                <tbody class="[&_tr:last-child]:border-0">
                  <tr v-for="comment in comments" :key="comment.commentId" class="border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted">
                    <td class="p-4 align-middle">{{ comment.commentId }}</td>
                    <td class="p-4 align-middle font-medium truncate max-w-[300px]">{{ comment.body }}</td>
                    <td class="p-4 align-middle">{{ comment.boardId }}</td>
                    <td class="p-4 align-middle">
                      {{ comment.userName }}
                      <span class="text-xs text-muted-foreground">({{ comment.bojId }})</span>
                    </td>
                    <td class="p-4 align-middle">
                      <Badge variant="destructive">{{ comment.warningCnt }}</Badge>
                    </td>
                    <td class="p-4 align-middle">
                      <Badge :variant="comment.visible === '1' ? 'outline' : 'secondary'">
                        {{ comment.visible === '1' ? '공개' : '숨김' }}
                      </Badge>
                    </td>
                    <td class="p-4 align-middle text-right space-x-2">
                      <Button 
                        variant="ghost" 
                        size="sm"
                        @click="handleDeactivateComment(comment.commentId)"
                        :disabled="comment.visible === '0'"
                      >
                        비활성화
                      </Button>
                      <Button 
                        variant="ghost" 
                        size="sm"
                        class="text-red-500 hover:text-red-600"
                        @click="handleDeactivateUser(comment.bojId)" 
                      >
                        유저 정지
                      </Button>
                    </td>
                  </tr>
                  <tr v-if="comments.length === 0">
                    <td colspan="7" class="p-4 align-middle h-24 text-center">
                      신고된 댓글이 없습니다.
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </TabsContent>
    </Tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getReportedBoards, getReportedComments, deactivateBoard, deactivateComment, deactivateUser } from '@/api/admin'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'

const boards = ref([])
const comments = ref([])

const fetchBoards = async () => {
  try {
    const res = await getReportedBoards()
    boards.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const fetchComments = async () => {
  try {
    const res = await getReportedComments()
    comments.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const handleDeactivateBoard = async (boardId) => {
  if (!confirm('정말 이 게시글을 비활성화(숨김) 처리하시겠습니까?')) return
  try {
    await deactivateBoard(boardId)
    alert('처리되었습니다.')
    fetchBoards()
  } catch (err) {
    alert('처리 실패')
  }
}

const handleDeactivateComment = async (commentId) => {
  if (!confirm('정말 이 댓글을 비활성화(숨김) 처리하시겠습니까?')) return
  try {
    await deactivateComment(commentId)
    alert('처리되었습니다.')
    fetchComments()
  } catch (err) {
    alert('처리 실패')
  }
}

const handleDeactivateUser = async (userId) => {
  if (!confirm('해당 유저를 비활성화(정지) 처리하시겠습니까?')) return
  try {
    await deactivateUser(userId) 
    alert('유저가 정지되었습니다.')
  } catch (err) {
    alert('처리 실패')
  }
}

onMounted(() => {
  fetchBoards()
  fetchComments()
})
</script>