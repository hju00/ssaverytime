<template>
  <div class="p-6 space-y-6 bg-muted/20 min-h-screen">
    <!-- Header Section -->
    <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
      <div>
        <h1 class="text-3xl font-bold tracking-tight">환영합니다, 싸피인님!</h1>
        <p class="text-muted-foreground">오늘의 싸피 소식을 확인해보세요.</p>
      </div>
      <div class="flex items-center gap-2">
        <Button variant="outline" @click="$router.push('/profile')">
          {{ $t('sidebar.profile') }}
        </Button>
      </div>
    </div>

    <!-- Quick Stats / Overview Cards -->
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      <Card class="bg-primary text-primary-foreground">
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">총 섭취 칼로리</CardTitle>
          <UtensilsIcon class="h-4 w-4 opacity-70" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">1,250 kcal</div>
          <p class="text-xs opacity-70">어제보다 +15% 증가</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">읽지 않은 공지</CardTitle>
          <BellIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">3</div>
          <p class="text-xs text-muted-foreground">캠퍼스 공지 2건, 일반 1건</p>
        </CardContent>
      </Card>

       <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">백준 스트릭</CardTitle>
          <TrophyIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">12일째</div>
          <p class="text-xs text-muted-foreground">꾸준함이 실력입니다!</p>
        </CardContent>
      </Card>
      
       <Card>
        <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle class="text-sm font-medium">마일리지</CardTitle>
          <StarIcon class="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div class="text-2xl font-bold">150 pts</div>
          <p class="text-xs text-muted-foreground">등급: Gold III</p>
        </CardContent>
      </Card>
    </div>

    <!-- Main Content Area -->
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
      
      <!-- Left Column: Today's Diet -->
      <Card class="col-span-4 lg:col-span-4">
        <CardHeader>
          <CardTitle>{{ $t('diet.title') }}</CardTitle>
          <CardDescription>{{ $t('diet.description') }}</CardDescription>
        </CardHeader>
        <CardContent class="pl-2">
          <!-- Mock Content for Diet Preview -->
          <div class="space-y-4 px-4">
             <div class="flex items-center p-4 border rounded-lg bg-background shadow-sm">
                <div class="w-12 h-12 rounded-full bg-green-100 flex items-center justify-center text-green-600 mr-4">
                  <UtensilsIcon class="w-6 h-6" />
                </div>
                <div class="flex-1">
                  <h4 class="font-semibold">점심 (한식 코너)</h4>
                  <p class="text-sm text-muted-foreground">제육볶음, 흑미밥, 배추김치, 미역국</p>
                </div>
                <Badge>850 kcal</Badge>
             </div>
             <div class="flex items-center p-4 border rounded-lg bg-background shadow-sm">
                <div class="w-12 h-12 rounded-full bg-orange-100 flex items-center justify-center text-orange-600 mr-4">
                   <CoffeeIcon class="w-6 h-6" />
                </div>
                <div class="flex-1">
                  <h4 class="font-semibold">오후 간식</h4>
                  <p class="text-sm text-muted-foreground">아이스 아메리카노</p>
                </div>
                <Badge variant="outline">10 kcal</Badge>
             </div>
          </div>
          
          <div class="mt-6 px-4">
             <Button class="w-full" variant="outline" @click="$router.push('/diet')">
               {{ $t('diet.import_ssafy_lunch') }}
             </Button>
          </div>
        </CardContent>
      </Card>

      <!-- Right Column: Recent Posts (Hot Posts) -->
      <Card class="col-span-3 lg:col-span-3">
        <CardHeader>
          <CardTitle>{{ $t('board.hot_posts') }}</CardTitle>
          <CardDescription>광주 캠퍼스 인기글</CardDescription>
        </CardHeader>
        <CardContent>
           <div class="space-y-4">
              <!-- Mock Post Items -->
              <div v-for="i in 3" :key="i" class="flex flex-col gap-1 pb-4 border-b last:border-0 cursor-pointer hover:bg-muted/50 p-2 rounded-md transition-colors" @click="$router.push('/board')">
                 <div class="flex justify-between items-start">
                    <h4 class="font-medium text-sm">알고리즘 스터디원 모집합니다!</h4>
                    <Badge variant="secondary" class="text-xs">스터디</Badge>
                 </div>
                 <p class="text-xs text-muted-foreground line-clamp-1">
                    오전 알고리즘 스터디 부원 2명 충원합니다. 백준 골드 이상...
                 </p>
                 <div class="flex gap-3 text-xs text-muted-foreground mt-1">
                    <span class="flex items-center gap-1"><HeartIcon class="w-3 h-3" /> 12</span>
                    <span class="flex items-center gap-1"><MessageSquareIcon class="w-3 h-3" /> 5</span>
                 </div>
              </div>
           </div>
           
           <div class="mt-4">
             <Button class="w-full" @click="$router.push('/board')">
               전체 게시글 보기
             </Button>
           </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { 
  Utensils as UtensilsIcon, 
  Bell as BellIcon, 
  Trophy as TrophyIcon, 
  Star as StarIcon,
  Coffee as CoffeeIcon,
  Heart as HeartIcon,
  MessageSquare as MessageSquareIcon
} from 'lucide-vue-next'
</script>

<style scoped>
</style>