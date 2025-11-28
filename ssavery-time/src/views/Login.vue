<template>
  <div class="min-h-screen bg-background flex items-center justify-center p-4">
    <Card class="w-full max-w-md shadow-lg">
      <CardHeader class="space-y-2">
        <div class="flex items-center justify-center gap-2 mb-4">
          <div class="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
            <span class="text-primary-foreground font-bold">S</span>
          </div>
          <CardTitle class="text-2xl">{{ $t('home.title') }}</CardTitle>
        </div>
        <CardDescription class="text-center">{{ $t('home.login_description') || 'Welcome back! Please login to continue.' }}</CardDescription>
      </CardHeader>

      <CardContent class="space-y-4">
        <div class="space-y-2">
          <Label for="login-id" class="text-sm font-medium">
            {{ $t('home.login_id_label') }}
          </Label>
          <Input
            id="login-id"
            name="id"
            :placeholder="$t('home.login_id_placeholder')"
            v-model="loginData.id"
            class="rounded-lg border-input bg-background h-11"
          />
        </div>

        <div class="space-y-2">
          <Label for="login-password" class="text-sm font-medium">
            {{ $t('home.login_password_label') }}
          </Label>
          <Input
            id="login-password"
            name="password"
            type="password"
            :placeholder="$t('home.login_password_placeholder')"
            v-model="loginData.password"
            class="rounded-lg border-input bg-background h-11"
          />
        </div>

        <Button
          @click="handleLogin"
          class="w-full h-11 rounded-lg bg-primary text-primary-foreground font-medium mt-6 hover:bg-primary/90"
        >
          {{ $t('home.login') }}
        </Button>

        <div class="text-center text-sm mt-4">
          <span class="text-muted-foreground">Don't have an account? </span>
          <router-link to="/signup" class="text-primary font-medium hover:underline">
            {{ $t('home.signup') }}
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

const router = useRouter()
const loginData = ref({ id: '', password: '' })

const handleLogin = () => {
  console.log('Login:', loginData.value)
  // Simulate login success
  router.push('/')
}
</script>
