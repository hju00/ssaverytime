<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <Card class="w-full max-w-md shadow-lg">
      <CardHeader class="space-y-2 text-center">
        <div class="flex items-center justify-center gap-2 mb-2">
          <div class="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
            <span class="text-primary-foreground font-bold">S</span>
          </div>
        </div>
        <CardTitle class="text-2xl">{{ $t('home.title') }}</CardTitle>
        <CardDescription>
          {{ activeTab === 'login' ? '로그인하여 서비스를 이용하세요.' : '계정을 생성하고 함께하세요.' }}
        </CardDescription>
      </CardHeader>

      <CardContent>
        <Tabs v-model="activeTab" class="w-full">
          <TabsList class="grid w-full grid-cols-2 mb-6">
            <TabsTrigger value="login">로그인</TabsTrigger>
            <TabsTrigger value="signup">회원가입</TabsTrigger>
          </TabsList>

          <TabsContent value="login" class="space-y-4">
            <div class="space-y-2">
              <Label for="login-id">{{ $t('home.login_id_label') }}</Label>
              <Input
                id="login-id"
                v-model="loginData.id"
                :placeholder="$t('home.login_id_placeholder')"
                class="h-11"
              />
            </div>
            <div class="space-y-2">
              <Label for="login-password">{{ $t('home.login_password_label') }}</Label>
              <Input
                id="login-password"
                type="password"
                v-model="loginData.password"
                :placeholder="$t('home.login_password_placeholder')"
                class="h-11"
              />
            </div>
            <Button class="w-full h-11 mt-4" @click="handleLogin">
              {{ $t('home.login') }}
            </Button>
          </TabsContent>

          <TabsContent value="signup" class="space-y-4">
            <div class="space-y-2">
              <Label for="signup-id">{{ $t('home.signup_id_label') }}</Label>
              <Input
                id="signup-id"
                v-model="signupData.id"
                :placeholder="$t('home.signup_id_placeholder')"
                class="h-11"
              />
            </div>
            <div class="space-y-2">
              <Label for="signup-password">{{ $t('home.signup_password_label') }}</Label>
              <Input
                id="signup-password"
                type="password"
                v-model="signupData.password"
                :placeholder="$t('home.signup_password_placeholder')"
                class="h-11"
              />
            </div>
            <div class="space-y-2">
              <Label for="confirm-password">{{ $t('home.signup_confirm_password_label') }}</Label>
              <Input
                id="confirm-password"
                type="password"
                v-model="signupData.confirmPassword"
                :placeholder="$t('home.signup_confirm_password_placeholder')"
                class="h-11"
              />
            </div>
            <div class="space-y-2">
              <Label for="nickname">{{ $t('home.signup_nickname_label') }}</Label>
              <Input
                id="nickname"
                v-model="signupData.nickname"
                :placeholder="$t('home.signup_nickname_placeholder')"
                class="h-11"
              />
            </div>
            <div class="space-y-2">
              <Label for="campus">{{ $t('home.signup_campus_label') }}</Label>
              <Select v-model="signupData.campus">
                <SelectTrigger id="campus" class="h-11">
                  <SelectValue :placeholder="$t('home.signup_campus_placeholder')" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="gwangju">광주</SelectItem>
                  <SelectItem value="seoul">서울</SelectItem>
                  <SelectItem value="daejeon">대전</SelectItem>
                  <SelectItem value="gumi">구미</SelectItem>
                  <SelectItem value="buil">부울경</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div class="space-y-2">
              <Label for="baekjoon">{{ $t('home.signup_baekjoon_label') }}</Label>
              <div class="flex gap-2">
                <Input
                  id="baekjoon"
                  v-model="signupData.baekjoonId"
                  :placeholder="$t('home.signup_baekjoon_placeholder')"
                  class="h-11 flex-1"
                />
                <Button variant="outline" class="h-11 px-4" @click="handleVerifyBaekjoon">
                  {{ $t('home.verify_button') }}
                </Button>
              </div>
            </div>
            <Button class="w-full h-11 mt-4" @click="handleSignup">
              {{ $t('home.create_account_button') }}
            </Button>
          </TabsContent>
        </Tabs>
      </CardContent>
    </Card>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
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
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@/components/ui/tabs'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

const route = useRoute()
const router = useRouter()

// Initialize activeTab based on current route name
// 'Login' route -> 'login' tab, 'Signup' route -> 'signup' tab
const activeTab = ref(route.name === 'Signup' ? 'signup' : 'login')

// Form Data
const loginData = ref({ id: '', password: '' })
const signupData = ref({
  id: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  campus: '',
  baekjoonId: '',
})

// Sync activeTab changes to URL
watch(activeTab, (newTab) => {
  const targetRouteName = newTab === 'login' ? 'Login' : 'Signup'
  if (route.name !== targetRouteName) {
    router.replace({ name: targetRouteName })
  }
})

// Sync URL changes to activeTab (e.g. browser back button)
watch(
  () => route.name,
  (newName) => {
    const targetTab = newName === 'Signup' ? 'signup' : 'login'
    if (activeTab.value !== targetTab) {
      activeTab.value = targetTab
    }
  }
)

const handleLogin = () => {
  console.log('Login:', loginData.value)
  router.push('/')
}

const handleSignup = () => {
  console.log('Signup:', signupData.value)
  // After signup, usually go to login, or auto-login.
  // For now, switch to login tab
  activeTab.value = 'login'
}

const handleVerifyBaekjoon = () => {
  console.log('Verify Baekjoon:', signupData.value.baekjoonId)
}
</script>
