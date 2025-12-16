<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-2xl mx-auto space-y-6">
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">회원정보 수정</h2>
        </CardHeader>

        <CardContent class="space-y-4">
          <div class="space-y-2">
            <Label>닉네임</Label>
            <Input v-model="form.name" placeholder="닉네임" class="h-11" />
          </div>

          <div class="space-y-2">
            <Label>기수</Label>
            <Input v-model.number="form.season" type="number" placeholder="예: 14" class="h-11" />
          </div>

          <div class="space-y-2">
            <Label>새 비밀번호 (선택)</Label>
            <Input v-model="form.password" type="password" placeholder="변경 시에만 입력" class="h-11" />
          </div>

          <div class="flex gap-2 pt-2">
            <Button variant="outline" class="bg-transparent" @click="router.back()">취소</Button>
            <Button :disabled="saving" @click="handleSave">
              {{ saving ? '저장중...' : '저장' }}
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg text-red-600">회원 탈퇴</h2>
        </CardHeader>

        <CardContent class="space-y-3">
          <p class="text-sm text-muted-foreground">
            탈퇴하면 계정이 비활성(INVALID) 처리됩니다.
          </p>

          <Button
            variant="destructive"
            class="w-full"
            :disabled="withdrawing"
            @click="handleWithdraw"
          >
            {{ withdrawing ? '처리중...' : '탈퇴하기' }}
          </Button>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyPage, updateMyPage, withdraw } from '@/api/mypage'

import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

const router = useRouter()
const saving = ref(false)
const withdrawing = ref(false)

const form = ref({
  name: '',
  season: 0,
  password: '',
})

const load = async () => {
  const res = await getMyPage()
  form.value = {
    name: res.data.name ?? '',
    season: res.data.season ?? 0,
    password: '',
  }
}

const handleSave = async () => {
  if (!form.value.name) {
    alert('닉네임을 입력해주세요.')
    return
  }

  saving.value = true
  try {
    const payload = {
      name: form.value.name,
      season: Number(form.value.season),
    }

    // password는 입력했을 때만 보냄
    if (form.value.password && form.value.password.trim().length > 0) {
      payload.password = form.value.password
    }

    await updateMyPage(payload)
    alert('수정이 완료되었습니다.')
    router.push('/profile')
  } catch (e) {
    console.error(e)
    alert('수정에 실패했습니다.')
  } finally {
    saving.value = false
  }
}

const handleWithdraw = async () => {
  const ok = confirm('정말 탈퇴하시겠습니까?')
  if (!ok) return

  withdrawing.value = true
  try {
    await withdraw()

    localStorage.removeItem('accessToken')
    window.dispatchEvent(new Event('auth-changed'))

    alert('탈퇴 처리되었습니다.')
    router.push('/')
  } catch (e) {
    console.error(e)
    alert('탈퇴에 실패했습니다.')
  } finally {
    withdrawing.value = false
  }
}

onMounted(() => {
  load()
})
</script>
