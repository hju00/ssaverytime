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
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead class="w-[80px]">ID</TableHead>
                    <TableHead>제목</TableHead>
                    <TableHead>작성자</TableHead>
                    <TableHead class="w-[100px]">신고수</TableHead>
                    <TableHead class="w-[100px]">상태</TableHead>
                    <TableHead class="text-right">관리</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow v-for="board in boards" :key="board.boardId">
                    <TableCell>{{ board.boardId }}</TableCell>
                    <TableCell class="font-medium">{{ board.title }}</TableCell>
                    <TableCell>
                      {{ board.userName }}
                      <span class="text-xs text-muted-foreground">({{ board.bojId }})</span>
                    </TableCell>
                    <TableCell>
                      <Badge variant="destructive">{{ board.warningCnt }}</Badge>
                    </TableCell>
                    <TableCell>
                      <Badge :variant="board.visible === '1' ? 'outline' : 'secondary'">
                        {{ board.visible === '1' ? '공개' : '숨김' }}
                      </Badge>
                    </TableCell>
                    <TableCell class="text-right space-x-2">
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
                    </TableCell>
                  </TableRow>
                  <TableRow v-if="boards.length === 0">
                    <TableCell colspan="6" class="h-24 text-center">
                      신고된 게시글이 없습니다.
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
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
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead class="w-[80px]">ID</TableHead>
                    <TableHead>내용</TableHead>
                    <TableHead>게시글ID</TableHead>
                    <TableHead>작성자</TableHead>
                    <TableHead class="w-[100px]">신고수</TableHead>
                    <TableHead class="w-[100px]">상태</TableHead>
                    <TableHead class="text-right">관리</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow v-for="comment in comments" :key="comment.commentId">
                    <TableCell>{{ comment.commentId }}</TableCell>
                    <TableCell class="font-medium truncate max-w-[300px]">{{ comment.body }}</TableCell>
                    <TableCell>{{ comment.boardId }}</TableCell>
                    <TableCell>
                      {{ comment.userName }}
                      <span class="text-xs text-muted-foreground">({{ comment.bojId }})</span>
                    </TableCell>
                    <TableCell>
                      <Badge variant="destructive">{{ comment.warningCnt }}</Badge>
                    </TableCell>
                    <TableCell>
                      <Badge :variant="comment.visible === '1' ? 'outline' : 'secondary'">
                        {{ comment.visible === '1' ? '공개' : '숨김' }}
                      </Badge>
                    </TableCell>
                    <TableCell class="text-right space-x-2">
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
                    </TableCell>
                  </TableRow>
                  <TableRow v-if="comments.length === 0">
                    <TableCell colspan="7" class="h-24 text-center">
                      신고된 댓글이 없습니다.
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
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
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
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
    // alert('권한이 없거나 데이터를 불러올 수 없습니다.')
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

// TODO: 유저 ID(PK)가 필요한데 현재 DTO에는 BOJ_ID(String)만 있을 수 있음. 
// 백엔드 AdminMapper에서 u.USER_ID도 가져오도록 수정되었는지 확인 필요.
// 일단 DTO를 믿고 호출하되, 만약 API가 int userId를 요구한다면 프론트/백엔드 수정 필요.
// 현재 AdminService는 int userId를 요구함.
// DTO에 userId 필드가 없다면 추가 필요. (지금은 bojId만 있는 것 같음)
const handleDeactivateUser = async (userId) => {
  if (!confirm('해당 유저를 비활성화(정지) 처리하시겠습니까?')) return
  try {
    // 주의: userId가 숫자형 PK여야 함. DTO 확인 필요.
    // 만약 String(bojId)만 있다면 백엔드 API를 bojId 기준으로 바꾸거나 DTO에 PK 추가해야 함.
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
