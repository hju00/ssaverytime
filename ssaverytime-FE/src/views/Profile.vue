<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-2xl mx-auto space-y-6">
      <Card class="border-border text-center">
        <CardContent class="p-8 space-y-4">
          <div class="w-24 h-24 flex items-center justify-center mx-auto mb-4">
            <img
              v-if="tierSvgUrl"
              :src="tierSvgUrl"
              alt="Profile"
              class="w-full h-full object-contain animate-float"
            />
            <span v-else class="text-6xl animate-float">👤</span>
          </div>

          <div>
            <h1 class="text-2xl font-bold text-foreground">{{ user.nickname }}</h1>
            <p class="text-sm text-muted-foreground">{{ user.season }}기</p>
          </div>
        </CardContent>
      </Card>

      <Card class="border-border">
        <CardHeader class="flex flex-row items-center justify-between">
          <h2 class="font-bold text-lg">{{ $t('profile.baekjoon_tier') }}</h2>

          <Button
            variant="outline"
            class="bg-transparent"
            :disabled="refreshingRank"
            @click="handleRefreshRank"
          >
            {{ refreshingRank ? '갱신중...' : '랭크 갱신' }}
          </Button>
        </CardHeader>

        <CardContent class="space-y-4">
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 flex items-center justify-center">
              <img v-if="tierSvgUrl" :src="tierSvgUrl" alt="Tier" class="w-10 h-10 object-contain" />
            </div>
            <div>
              <p class="font-semibold text-lg">{{ user.tier || 'Unrated' }}</p>
              <p class="text-xs text-muted-foreground">{{ $t('profile.algorithm_rank') }}</p>
            </div>
          </div>

          <div class="pt-2">
            <Button variant="outline" class="bg-transparent w-full" @click="router.push('/profile/edit')">
              회원정보 수정 / 탈퇴
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyPage, refreshBojRank } from '@/api/mypage'
import { getTierNumber } from '@/lib/utils'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

const router = useRouter()

const user = ref({
  nickname: '',
  season: '',
  tier: '',      // 여기엔 svgUrl이 들어올 수도 있고, 이름이 들어올 수도 있음
  tierNumber: null,
})

// ✅ solved.ac 티어 이미지는 "번호"로 쓸 수도 있고, 백엔드가 svgUrl을 주면 그걸 그대로 쓰는게 제일 확실
const tierSvgUrl = computed(() => {
  // 1) 백엔드가 svgUrl을 주는 경우
  if (user.value.tier && user.value.tier.startsWith('http')) return user.value.tier

  // 2) 혹시 문자열이 tier 이름이면, 기존 util로 번호 계산해서 만든다(백엔드 응답 형태에 따라)
  const num = getTierNumber(user.value.tier)
  if (num) return `https://static.solved.ac/tier_small/${num}.svg`

  return ''
})

const refreshingRank = ref(false)

const fetchProfile = async () => {
  const res = await getMyPage()
  user.value = {
    nickname: res.data.name,
    season: res.data.season,
    tier: res.data.baekjoon, // 백엔드에서 저장된 BAEKJOON 값 (보통 svgUrl)
    tierNumber: getTierNumber(res.data.baekjoon),
  }
}

const handleRefreshRank = async () => {
  refreshingRank.value = true
  try {
    const res = await refreshBojRank()
    // 백엔드: return ResponseEntity.ok(new BojValidateResponseDto(svgUrl))
    // -> res.data.baekjoon 에 svgUrl 들어옴
    const svgUrl = res?.data?.baekjoon
    if (svgUrl) {
      user.value.tier = svgUrl
    }
    alert('랭크가 갱신되었습니다.')
  } catch (e) {
    console.error(e)
    alert('랭크 갱신에 실패했습니다.')
  } finally {
    refreshingRank.value = false
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
.animate-float {
  animation: float 3s ease-in-out infinite;
}
</style>
