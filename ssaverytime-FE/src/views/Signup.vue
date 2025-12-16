<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <Card class="w-full max-w-md shadow-lg my-8">
      <CardHeader class="space-y-2">
        <div class="flex items-center justify-center gap-2 mb-4">
          <div class="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
            <span class="text-primary-foreground font-bold">S</span>
          </div>
          <CardTitle class="text-2xl">회원가입</CardTitle>
        </div>
        <CardDescription class="text-center">
          Create an account to get started.
        </CardDescription>
      </CardHeader>

      <CardContent class="space-y-4">
        <!-- 백준 ID -->
        <div class="space-y-2">
          <Label class="text-sm font-medium">백준 ID</Label>

          <div class="flex gap-2">
            <Input
              v-model="signupData.id"
              placeholder="백준 아이디 입력"
              class="h-11 flex-1"
            />
            <Button
              variant="outline"
              :disabled="verifying || !signupData.id"
              @click="handleVerifyBaekjoon"
            >
              {{ verifying ? '확인중...' : isBojVerified ? '인증완료' : '인증' }}
            </Button>
          </div>

          <div
            v-if="isBojVerified"
            class="flex items-center gap-2 text-sm text-green-600"
          >
            <span>인증 성공</span>
            <img :src="signupData.baekjoon" alt="tier" class="h-5" />
          </div>

          <p v-else class="text-xs text-muted-foreground">
            * 백준 인증 후에만 회원가입이 가능합니다.
          </p>
        </div>

        <!-- 비밀번호 -->
        <Input
          type="password"
          placeholder="비밀번호"
          v-model="signupData.password"
          class="h-11"
        />

        <Input
          type="password"
          placeholder="비밀번호 확인"
          v-model="signupData.confirmPassword"
          class="h-11"
        />

        <!-- 닉네임 -->
        <Input
          placeholder="닉네임"
          v-model="signupData.nickname"
          class="h-11"
        />

        <!-- 캠퍼스 -->
        <Select v-model="signupData.campus">
          <SelectTrigger class="h-11">
            <SelectValue placeholder="캠퍼스 선택" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="seoul">Seoul</SelectItem>
            <SelectItem value="gwangju">Gwangju</SelectItem>
            <SelectItem value="daejeon">Daejeon</SelectItem>
            <SelectItem value="gumi">Gumi</SelectItem>
            <SelectItem value="buil">Buil</SelectItem>
          </SelectContent>
        </Select>

        <!-- 기수 -->
        <Select v-model="signupData.season">
          <SelectTrigger class="h-11">
            <SelectValue placeholder="기수 선택" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem
              v-for="s in seasonList"
              :key="s"
              :value="String(s)"
            >
              {{ s }}기
            </SelectItem>
          </SelectContent>
        </Select>

        <!-- 회원가입 버튼 -->
        <Button
          class="w-full h-11 mt-4"
          :disabled="!canSignup"
          @click="handleSignup"
        >
          계정 생성
        </Button>

        <div class="text-center text-sm">
          <router-link to="/login" class="text-primary hover:underline">
            로그인
          </router-link>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/api/http'

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

const router = useRouter()

/* ================= 상태 ================= */
const signupData = ref({
  id: '',          // BOJ_ID
  password: '',
  confirmPassword: '',
  nickname: '',
  campus: '',
  season: '',
  baekjoon: '',    // 티어 SVG URL
})

const verifying = ref(false)
const isBojVerified = ref(false)

/* ================= 백준 ID 변경 시 인증 무효 ================= */
watch(() => signupData.value.id, () => {
  isBojVerified.value = false
  signupData.value.baekjoon = ''
})

/* ================= 기수 목록 ================= */
const seasonList = Array.from({ length: 20 }, (_, i) => i + 1).reverse()

/* ================= 가입 가능 여부 ================= */
const canSignup = computed(() => {
  return (
    isBojVerified.value &&
    signupData.value.password &&
    signupData.value.password === signupData.value.confirmPassword &&
    signupData.value.nickname &&
    signupData.value.campus &&
    signupData.value.season
  )
})

/* ================= 백준 인증 ================= */
const handleVerifyBaekjoon = async () => {
  verifying.value = true
  try {
    const res = await http.get('/v1/auth/boj/validate', {
      params: { bojId: signupData.value.id },
    })

    signupData.value.baekjoon = res.data.baekjoon
    isBojVerified.value = true
    alert('백준 인증 성공')
  } catch (error) {
    isBojVerified.value = false
    signupData.value.baekjoon = ''
    alert('유효하지 않은 백준 아이디입니다.')
  } finally {
    verifying.value = false
  }
}

/* ================= 회원가입 ================= */
const handleSignup = async () => {
  await http.post('/v1/auth/regist', {
    bojId: signupData.value.id,
    password: signupData.value.password,
    name: signupData.value.nickname,
    campus: signupData.value.campus,
    season: Number(signupData.value.season),
    baekjoon: signupData.value.baekjoon,
  })

  alert('회원가입 완료')
  router.push('/login')
}
</script>
