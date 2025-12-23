<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { 
  Home as HomeIcon, 
  Clipboard as ClipboardIcon, 
  Forklift as ForkliftIcon, 
  User as UserIcon, 
  LogIn as LogInIcon, 
  LogOut as LogOutIcon, 
  Settings as SettingsIcon,
  Menu as MenuIcon 
} from 'lucide-vue-next'
import { Sheet, SheetContent, SheetTrigger } from '@/components/ui/sheet'
import { Button } from '@/components/ui/button'
import http from '@/api/http'

const router = useRouter()
const isLoggedIn = ref(false)
const isAdmin = ref(false)
const isMobileOpen = ref(false)

const checkLoginStatus = async () => {
  const token = localStorage.getItem('accessToken')
  isLoggedIn.value = !!token

  if (token) {
    try {
      const res = await http.get('/v1/mypage')
      if (res.data.role === 'ADMIN') {
        isAdmin.value = true
      } else {
        isAdmin.value = false
      }
    } catch (e) {
      console.error('Failed to fetch user info:', e)
      isAdmin.value = false
    }
  } else {
    isAdmin.value = false
  }
}

const handleLogout = () => {
  localStorage.removeItem('accessToken')
  isAdmin.value = false
  window.dispatchEvent(new Event('auth-changed'))
  router.push('/')
  isMobileOpen.value = false
}

// 모바일 메뉴 클릭 시 닫기
const closeMobileMenu = () => {
  isMobileOpen.value = false
}

onMounted(() => {
  checkLoginStatus()
  window.addEventListener('auth-changed', checkLoginStatus)
})

onUnmounted(() => {
  window.removeEventListener('auth-changed', checkLoginStatus)
})
</script>

<template>
  <div class="flex min-h-screen bg-background flex-col md:flex-row">
    <!-- Mobile Header -->
    <header class="md:hidden flex items-center justify-between p-4 border-b border-border bg-card sticky top-0 z-50">
      <div class="flex items-center gap-2">
        <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
          <span class="text-primary-foreground font-bold text-lg">S</span>
        </div>
        <h1 class="text-xl font-bold">{{ $t('home.title') }}</h1>
      </div>
      
      <Sheet v-model:open="isMobileOpen">
        <SheetTrigger as-child>
          <Button variant="ghost" size="icon">
            <MenuIcon class="w-6 h-6" />
          </Button>
        </SheetTrigger>
        <SheetContent side="left" class="w-64 p-0">
          <div class="flex flex-col h-full p-4">
            <div class="flex items-center gap-2 mb-6">
              <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
                <span class="text-primary-foreground font-bold text-lg">S</span>
              </div>
              <h1 class="text-xl font-bold">{{ $t('home.title') }}</h1>
            </div>
            
            <nav class="flex-1 space-y-2">
              <RouterLink to="/" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
                <HomeIcon class="w-5 h-5" />
                <span>{{ $t('sidebar.home') }}</span>
              </RouterLink>
              <RouterLink to="/board" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
                <ClipboardIcon class="w-5 h-5" />
                <span>{{ $t('sidebar.board') }}</span>
              </RouterLink>
              <RouterLink to="/diet" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
                <ForkliftIcon class="w-5 h-5" />
                <span>{{ $t('sidebar.diet') }}</span>
              </RouterLink>
              <RouterLink to="/profile" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
                <UserIcon class="w-5 h-5" />
                <span>{{ $t('sidebar.profile') }}</span>
              </RouterLink>
              
              <RouterLink v-if="isAdmin" to="/admin" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors text-red-500 hover:text-red-600">
                <SettingsIcon class="w-5 h-5" />
                <span>관리자 페이지</span>
              </RouterLink>

              <div class="my-4 border-t border-border"></div>

              <RouterLink v-if="!isLoggedIn" to="/login" @click="closeMobileMenu" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
                <LogInIcon class="w-5 h-5" />
                <span>{{ $t('home.login') }}</span>
              </RouterLink>

              <button v-else @click="handleLogout" class="w-full flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors text-left">
                <LogOutIcon class="w-5 h-5" />
                <span>Logout</span>
              </button>
            </nav>
          </div>
        </SheetContent>
      </Sheet>
    </header>

    <!-- Desktop Sidebar -->
    <aside class="hidden md:flex w-64 bg-card text-card-foreground border-r border-border p-4 flex-col h-screen sticky top-0">
      <div class="flex items-center gap-2 mb-6">
        <div class="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
          <span class="text-primary-foreground font-bold text-lg">S</span>
        </div>
        <h1 class="text-xl font-bold">{{ $t('home.title') }}</h1>
      </div>

      <nav class="flex-1 space-y-2">
        <RouterLink to="/" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
          <HomeIcon class="w-5 h-5" />
          <span>{{ $t('sidebar.home') }}</span>
        </RouterLink>
        <RouterLink to="/board" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
          <ClipboardIcon class="w-5 h-5" />
          <span>{{ $t('sidebar.board') }}</span>
        </RouterLink>
        <RouterLink to="/diet" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
          <ForkliftIcon class="w-5 h-5" />
          <span>{{ $t('sidebar.diet') }}</span>
        </RouterLink>
        <RouterLink to="/profile" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
          <UserIcon class="w-5 h-5" />
          <span>{{ $t('sidebar.profile') }}</span>
        </RouterLink>
        
        <RouterLink v-if="isAdmin" to="/admin" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors text-red-500 hover:text-red-600">
          <SettingsIcon class="w-5 h-5" />
          <span>관리자 페이지</span>
        </RouterLink>

        <div class="my-4 border-t border-border"></div>

        <RouterLink v-if="!isLoggedIn" to="/login" class="flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors">
          <LogInIcon class="w-5 h-5" />
          <span>{{ $t('home.login') }}</span>
        </RouterLink>

        <button v-else @click="handleLogout" class="w-full flex items-center gap-2 p-2 rounded-md hover:bg-muted/50 transition-colors text-left">
          <LogOutIcon class="w-5 h-5" />
          <span>Logout</span>
        </button>
      </nav>

      <div class="mt-auto pt-4 border-t border-border">
        <p class="text-sm text-muted-foreground">{{ $t('sidebar.copyright') }}</p>
      </div>
    </aside>

    <!-- Main Content Area -->
    <main class="flex-1 overflow-auto w-full">
      <router-view />
    </main>
  </div>
</template>

<style>
/* Global styles for App.vue. Tailwind handles most of it. */
/* You might want to define custom scrollbar styles here if needed */
</style>