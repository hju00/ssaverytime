<template>
  <div class="flex min-h-screen bg-background">
    <!-- Sidebar -->
    <aside class="w-64 bg-card text-card-foreground border-r border-border p-4 flex flex-col">
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
    <main class="flex-1 overflow-auto">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { Home as HomeIcon, Clipboard as ClipboardIcon, Forklift as ForkliftIcon, FileText as FileTextIcon, User as UserIcon, LogIn as LogInIcon, LogOut as LogOutIcon } from 'lucide-vue-next'

const router = useRouter()
const isLoggedIn = ref(false)

const checkLoginStatus = () => {
  isLoggedIn.value = !!localStorage.getItem('accessToken')
}

const handleLogout = () => {
  localStorage.removeItem('accessToken')
  window.dispatchEvent(new Event('auth-changed'))
  router.push('/')
}

onMounted(() => {
  checkLoginStatus()
  window.addEventListener('auth-changed', checkLoginStatus)
})

onUnmounted(() => {
  window.removeEventListener('auth-changed', checkLoginStatus)
})
</script>

<style>
/* Global styles for App.vue. Tailwind handles most of it. */
/* You might want to define custom scrollbar styles here if needed */
</style>