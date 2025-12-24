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
                      <Badge :variant="board.valid === '1' ? 'outline' : 'secondary'">
                        {{ board.valid === '1' ? '활성' : '비활성' }}
                      </Badge>
                    </td>
                    <td class="p-4 align-middle text-right space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        @click="handleDeactivateBoard(board.boardId)"
                        :disabled="board.valid === '0'"
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
                      <Badge :variant="comment.valid === '1' ? 'outline' : 'secondary'">
                        {{ comment.valid === '1' ? '활성' : '비활성' }}
                      </Badge>
                    </td>
                    <td class="p-4 align-middle text-right space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        @click="handleDeactivateComment(comment.commentId)"
                        :disabled="comment.valid === '0'"
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

    <!-- ✅ 메뉴 추가 섹션 -->
    <Card>
      <CardHeader>
        <CardTitle>식당 메뉴 추가</CardTitle>
        <CardDescription>
          식당별로 메뉴/칼로리/날짜를 입력하고 한 번에 여러 메뉴를 등록할 수 있습니다.
        </CardDescription>
      </CardHeader>

      <CardContent class="space-y-6">
        <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
          <Card
            v-for="r in restaurants"
            :key="r.id"
            class="border"
          >
            <CardHeader class="pb-3">
              <CardTitle class="text-base">{{ r.name }}</CardTitle>
              <CardDescription class="text-xs">restaurantId: {{ r.id }}</CardDescription>
            </CardHeader>

            <CardContent class="space-y-3">
              <!-- rows -->
              <div v-for="(row, idx) in menuForms[r.id]" :key="idx" class="space-y-2 p-3 rounded-md border bg-background">
                <div class="space-y-2">
                  <Input v-model="row.menu" placeholder="메뉴명" class="h-9" />
                  <Input v-model="row.calorie" type="number" placeholder="칼로리" class="h-9" />
                  <!-- ✅ 날짜/시간 row별 입력 -->
                  <Input v-model="row.dateLocal" type="datetime-local" class="h-9" />
                </div>

                <div class="flex justify-end">
                  <Button
                    variant="ghost"
                    size="sm"
                    class="text-red-500 hover:text-red-600"
                    @click="removeRow(r.id, idx)"
                    :disabled="menuForms[r.id].length <= 1 || savingRestaurantId === r.id"
                  >
                    삭제
                  </Button>
                </div>
              </div>

              <div class="flex gap-2 pt-1">
                <Button
                  variant="outline"
                  class="w-full"
                  @click="addRow(r.id)"
                  :disabled="savingRestaurantId === r.id"
                >
                  + 메뉴 추가
                </Button>
              </div>

              <div class="pt-2">
                <Button
                  class="w-full"
                  @click="submitRestaurantMenus(r.id)"
                  :disabled="savingRestaurantId === r.id"
                >
                  {{ savingRestaurantId === r.id ? '저장 중...' : '해당 식당 메뉴 저장' }}
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>

        <p class="text-xs text-muted-foreground">
          * 날짜는 관리자 입력값을 그대로 저장합니다. (예: 2025-11-07 12:00:00)
        </p>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getReportedBoards, getReportedComments, deactivateBoard, deactivateComment, deactivateUser } from '@/api/admin'
import http from '@/api/http'

import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'

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

/* ====================== 메뉴 추가 로직 ====================== */

const restaurants = ref([
  { id: 1, name: '육수고집' },
  { id: 2, name: '소담상' },
  { id: 3, name: '더고메' },
  { id: 4, name: '차이나호' },
  { id: 5, name: '속이찬새참' },
])

// restaurantId별 입력 폼들
const menuForms = ref({
  1: [createEmptyRow()],
  2: [createEmptyRow()],
  3: [createEmptyRow()],
  4: [createEmptyRow()],
  5: [createEmptyRow()],
})

function createEmptyRow() {
  return {
    menu: '',
    calorie: '',
    dateLocal: '', // datetime-local (YYYY-MM-DDTHH:mm)
  }
}

const addRow = (restaurantId) => {
  menuForms.value[restaurantId].push(createEmptyRow())
}

const removeRow = (restaurantId, idx) => {
  if (menuForms.value[restaurantId].length <= 1) return
  menuForms.value[restaurantId].splice(idx, 1)
}

// datetime-local -> "YYYY-MM-DD HH:mm:00"
const toServerDateTime = (dateLocal) => {
  if (!dateLocal) return ''
  // dateLocal: "2025-11-07T12:00"
  const [d, t] = dateLocal.split('T')
  if (!d || !t) return ''
  return `${d} ${t}:00`
}

const savingRestaurantId = ref(null)

const submitRestaurantMenus = async (restaurantId) => {
  const rows = menuForms.value[restaurantId]

  // 유효성 검사
  const invalid = rows.some((r) => {
    const menuOk = (r.menu || '').trim().length > 0
    const calOk = r.calorie !== '' && !Number.isNaN(Number(r.calorie))
    const dateOk = (r.dateLocal || '').trim().length > 0
    return !(menuOk && calOk && dateOk)
  })

  if (invalid) {
    alert('메뉴명/칼로리/날짜(시간)를 모두 입력해주세요.')
    return
  }

  savingRestaurantId.value = restaurantId

  try {
    const requests = rows.map((r) => {
      return http.post('/v1/diet/menu', {
        restaurantId,
        menu: r.menu.trim(),
        calorie: Number(r.calorie),
        date: toServerDateTime(r.dateLocal),
      })
    })

    // ✅ 한 식당의 여러 메뉴를 동시에 요청
    await Promise.all(requests)

    alert('메뉴가 등록되었습니다.')

    // 해당 식당 폼 초기화
    menuForms.value[restaurantId] = [createEmptyRow()]
  } catch (e) {
    console.error(e)
    alert('메뉴 등록에 실패했습니다.')
  } finally {
    savingRestaurantId.value = null
  }
}

/* ============================================================ */

onMounted(() => {
  fetchBoards()
  fetchComments()
})
</script>
