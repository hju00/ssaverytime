<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <Card class="w-full max-w-md shadow-lg my-8">
      <CardHeader class="space-y-2">
        <div class="flex items-center justify-center gap-2 mb-4">
          <div class="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
            <span class="text-primary-foreground font-bold">S</span>
          </div>
          <CardTitle class="text-2xl">{{ $t('home.signup') }}</CardTitle>
        </div>
        <CardDescription class="text-center">Create an account to get started.</CardDescription>
      </CardHeader>

      <CardContent class="space-y-4">
        <!-- Baekjoon ID (Used as User ID) -->
        <div class="space-y-2">
          <Label for="baekjoon" class="text-sm font-medium">
            {{ $t('home.signup_baekjoon_label') }} (아이디)
          </Label>
          <div class="flex gap-2">
            <Input
              id="baekjoon"
              name="baekjoonId"
              :placeholder="$t('home.signup_baekjoon_placeholder')"
              v-model="signupData.id"
              class="rounded-lg border-input bg-background h-11 flex-1"
            />
            <Button
              @click="handleVerifyBaekjoon"
              variant="outline"
              class="rounded-lg border-input h-11 px-4 bg-transparent"
            >
              {{ $t('home.verify_button') }}
            </Button>
          </div>
        </div>

        <div class="space-y-2">
          <Label for="signup-password" class="text-sm font-medium">
            {{ $t('home.signup_password_label') }}
          </Label>
          <Input
            id="signup-password"
            name="password"
            type="password"
            :placeholder="$t('home.signup_password_placeholder')"
            v-model="signupData.password"
            class="rounded-lg border-input bg-background h-11"
          />
        </div>

        <div class="space-y-2">
          <Label for="confirm-password" class="text-sm font-medium">
            {{ $t('home.signup_confirm_password_label') }}
          </Label>
          <Input
            id="confirm-password"
            name="confirmPassword"
            type="password"
            :placeholder="$t('home.signup_confirm_password_placeholder')"
            v-model="signupData.confirmPassword"
            class="rounded-lg border-input bg-background h-11"
          />
        </div>

        <div class="space-y-2">
          <Label for="nickname" class="text-sm font-medium">
            {{ $t('home.signup_nickname_label') }}
          </Label>
          <Input
            id="nickname"
            name="nickname"
            :placeholder="$t('home.signup_nickname_placeholder')"
            v-model="signupData.nickname"
            class="rounded-lg border-input bg-background h-11"
          />
        </div>

        <div class="space-y-2">
          <Label for="campus" class="text-sm font-medium">
            {{ $t('home.signup_campus_label') }}
          </Label>
          <Select v-model="signupData.campus">
            <SelectTrigger id="campus" class="rounded-lg border-input bg-background h-11">
              <SelectValue :placeholder="$t('home.signup_campus_placeholder')" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="gwangju">Gwangju</SelectItem>
              <SelectItem value="seoul">Seoul</SelectItem>
              <SelectItem value="daejeon">Daejeon</SelectItem>
              <SelectItem value="gumi">Gumi</SelectItem>
              <SelectItem value="buil">Buil</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <div class="space-y-2">
          <Label for="season" class="text-sm font-medium">
            기수
          </Label>
          <Select v-model="signupData.season">
            <SelectTrigger id="season" class="rounded-lg border-input bg-background h-11">
              <SelectValue placeholder="기수를 선택하세요" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem v-for="s in seasonList" :key="s" :value="String(s)">{{ s }}기</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <Button
          @click="handleSignup"
          class="w-full h-11 rounded-lg bg-primary text-primary-foreground font-medium mt-6 hover:bg-primary/90"
        >
          {{ $t('home.create_account_button') }}
        </Button>

        <div class="text-center text-sm mt-4">
          <span class="text-muted-foreground">Already have an account? </span>
          <router-link to="/login" class="text-primary font-medium hover:underline">
            {{ $t('home.login') }}
          </router-link>
        </div>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
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
const signupData = ref({
  id: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  campus: '',
  season: '',
  baekjoonId: '',
})

const generateSeasons = () => {
  const seasons = []
  const startYear = 2019
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = now.getMonth() + 1

  let maxSeason = (currentYear - startYear) * 2
  if (currentMonth >= 7) {
    maxSeason += 2
  } else {
    maxSeason += 1
  }

  for (let i = maxSeason; i >= 1; i--) {
    seasons.push(i)
  }
  return seasons
}

const seasonList = generateSeasons()

const handleSignup = async () => {
  if (signupData.value.password !== signupData.value.confirmPassword) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }

  if (!signupData.value.season) {
    alert('기수를 선택해주세요.')
    return
  }

  try {
    await http.post('/v1/auth/regist', {
      bojId: signupData.value.id,
      password: signupData.value.password,
      name: signupData.value.nickname,
      season: parseInt(signupData.value.season),
      baekjoon: signupData.value.id 
    })
    alert('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (error) {
    console.error('Signup failed:', error)
    if (error.response && error.response.status === 409) {
      alert('이미 존재하는 아이디입니다.')
    } else {
      alert('회원가입에 실패했습니다.')
    }
  }
}

const handleVerifyBaekjoon = async () => {
  if (!signupData.value.id) {
    alert('백준 아이디를 입력해주세요.')
    return
  }
  try {
    const response = await http.get('/v1/auth/boj/validate', {
      data: { bojId: signupData.value.id }
    })
    alert('확인되었습니다. 티어 정보를 가져왔습니다.')
  } catch (error) {
    alert('유효하지 않은 백준 아이디입니다.')
  }
}
</script>
