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
        <div class="space-y-2">
          <Label for="signup-id" class="text-sm font-medium">
            {{ $t('home.signup_id_label') }}
          </Label>
          <Input
            id="signup-id"
            name="id"
            :placeholder="$t('home.signup_id_placeholder')"
            v-model="signupData.id"
            class="rounded-lg border-input bg-background h-11"
          />
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
          <Label for="baekjoon" class="text-sm font-medium">
            {{ $t('home.signup_baekjoon_label') }}
          </Label>
          <div class="flex gap-2">
            <Input
              id="baekjoon"
              name="baekjoonId"
              :placeholder="$t('home.signup_baekjoon_placeholder')"
              v-model="signupData.baekjoonId"
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
  baekjoonId: '',
})

const handleSignup = () => {
  console.log('Signup:', signupData.value)
  // Simulate signup success
  router.push('/login')
}

const handleVerifyBaekjoon = () => {
  console.log('Verifying Baekjoon ID:', signupData.value.baekjoonId)
}
</script>
