<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-2xl mx-auto space-y-6">
      <!-- Profile Card -->
      <Card class="border-border text-center">
        <CardContent class="p-8 space-y-4">
          <div class="w-20 h-20 rounded-full bg-primary flex items-center justify-center text-5xl mx-auto">
            {{ user.avatar }}
          </div>
          <div>
            <h1 class="text-2xl font-bold text-foreground">{{ user.nickname }}</h1>
            <img :src="`https://static.solved.ac/tier_small/${user.tierNumber}.svg`" alt="Tier Icon" class="w-6 h-6 inline-block ml-2" />
            <p class="text-sm text-muted-foreground">{{ user.campus }} Campus</p>
          </div>
        </CardContent>
      </Card>

      <!-- Rank Section -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('profile.baekjoon_tier') }}</h2>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-3">
              <div class="w-12 h-12 rounded-lg bg-yellow-100 flex items-center justify-center font-bold text-yellow-800">
                AU
              </div>
              <div>
                <p class="font-semibold text-lg">{{ user.tier }}</p>
                <p class="text-xs text-muted-foreground">{{ $t('profile.algorithm_rank') }}</p>
              </div>
            </div>
            <span class="text-sm font-semibold text-primary">{{ user.currentRating }}</span>
          </div>
          <div class="space-y-2">
            <div class="flex items-center justify-between text-sm">
              <span class="text-muted-foreground">{{ $t('profile.progress_to_next_tier') }}</span>
              <span class="font-semibold">
                {{ progressToNextTierPercentage }}%
              </span>
            </div>
            <Progress :model-value="progressToNextTierPercentage" class="h-2 rounded-full" />
            <p class="text-xs text-muted-foreground text-right">
              {{ user.nextTierThreshold - user.currentRating }} {{ $t('profile.points_until_next_tier') }}
            </p>
          </div>
        </CardContent>
      </Card>

      <!-- Menu Grid -->
      <div class="grid grid-cols-2 gap-3">
        <Button
          v-for="(item, i) in menuItems"
          :key="i"
          variant="outline"
          class="h-24 flex flex-col items-start justify-between p-4 rounded-lg border-border hover:bg-muted hover:border-primary group bg-transparent"
        >
          <component :is="item.icon" class="w-6 h-6 text-primary group-hover:text-primary/80" />
          <div class="text-left">
            <p class="font-semibold text-sm text-foreground">{{ $t(item.label) }}</p>
            <p v-if="item.count !== null" class="text-xs text-muted-foreground">{{ item.count }}</p>
          </div>
        </Button>
      </div>

      <!-- Scrapped Posts Preview -->
      <Card class="border-border">
        <CardHeader>
          <h2 class="font-bold text-lg">{{ $t('profile.recently_scrapped') }}</h2>
        </CardHeader>
        <CardContent class="space-y-3">
          <div
            v-for="post in scrappedPosts"
            :key="post.id"
            class="flex items-start justify-between p-3 hover:bg-muted rounded-lg transition-colors cursor-pointer group"
          >
            <div class="flex-1">
              <h3 class="font-medium text-sm text-foreground line-clamp-2 group-hover:text-primary">
                {{ post.title }}
              </h3>
              <p class="text-xs text-muted-foreground mt-1">{{ $t('profile.by_prefix') }} {{ post.author }}</p>
            </div>
            <ChevronRightIcon class="w-4 h-4 text-muted-foreground flex-shrink-0 mt-1" />
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Progress } from '@/components/ui/progress'
import {
  FileText as FileTextIcon,
  MessageSquare as MessageSquareIcon,
  Bookmark as BookmarkIcon,
  Settings as SettingsIcon,
  ChevronRight as ChevronRightIcon,
} from 'lucide-vue-next'

const user = ref({
  nickname: 'StudyBuddy',
  campus: 'Seoul',
  avatar: '👤',
  tierNumber: 15, // Example: 15 corresponds to Gold 1
  currentRating: 1850,
  nextTierThreshold: 2000,
})

const menuItems = ref([
  { icon: FileTextIcon, label: 'My Posts', count: 24 },
  { icon: MessageSquareIcon, label: 'My Comments', count: 87 },
  { icon: BookmarkIcon, label: 'Scrapped Posts', count: 12 },
  { icon: SettingsIcon, label: 'Edit Profile', count: null },
])

const scrappedPosts = ref([
  {
    id: 1,
    title: 'Top 10 Algorithm tips for beginners',
    author: 'CodeMaster',
  },
  {
    id: 2,
    title: 'Best resources for learning System Design',
    author: 'DesignPro',
  },
  {
    id: 3,
    title: 'How to prepare for coding interviews',
    author: 'InterviewExpert',
  },
])

const progressToNextTierPercentage = computed(() => {
  return Math.round((user.value.currentRating / user.value.nextTierThreshold) * 100)
})
</script>

<style scoped>
/* Scoped styles for Profile.vue */
</style>