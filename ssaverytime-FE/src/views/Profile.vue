<template>
  <div class="min-h-screen bg-background p-4 pb-12">
    <div class="max-w-2xl mx-auto space-y-6">
      <!-- Profile Card -->
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

      <!-- Rank Section -->
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

      <!-- ✅ Scrapped Posts Preview (복구) -->
      <Card class="border-border">
        <CardHeader class="flex flex-row items-center justify-between">
          <h2 class="font-bold text-lg">스크랩한 게시글</h2>
          <Button variant="outline" class="bg-transparent" :disabled="loadingScraps" @click="fetchScraps">
            {{ loadingScraps ? '불러오는 중...' : '새로고침' }}
          </Button>
        </CardHeader>

        <CardContent class="space-y-3">
          <div
            v-for="post in scrappedPosts"
            :key="post.boardId"
            @click="$router.push(`/post/${post.boardId}`)"
            class="flex items-start justify-between p-3 hover:bg-muted rounded-lg transition-colors cursor-pointer group"
          >
            <div class="flex-1">
              <h3 class="font-medium text-sm text-foreground line-clamp-2 group-hover:text-primary">
                {{ post.title }}
              </h3>
              <p class="text-xs text-muted-foreground mt-1">by {{ post.userName }}</p>
            </div>
          </div>

          <div v-if="scrappedPosts.length === 0" class="text-center text-sm text-muted-foreground py-4">
            스크랩한 게시글이 없습니다.
          </div>
        </CardContent>
      </Card>

      <!-- Social Section -->
      <Card class="border-border">
        <CardHeader class="flex flex-row items-center justify-between">
          <h2 class="font-bold text-lg">소셜</h2>
          <Button variant="outline" class="bg-transparent" :disabled="loadingSocial" @click="reloadSocial">
            {{ loadingSocial ? '불러오는 중...' : '새로고침' }}
          </Button>
        </CardHeader>

        <CardContent class="space-y-6">
          <!-- Followers / Followings -->
          <div class="grid grid-cols-2 gap-3">
            <div class="p-4 rounded-lg border border-border bg-card">
              <p class="text-sm text-muted-foreground">팔로워</p>
              <p class="text-2xl font-bold">{{ followers.length }}</p>
            </div>
            <div class="p-4 rounded-lg border border-border bg-card">
              <p class="text-sm text-muted-foreground">팔로잉</p>
              <p class="text-2xl font-bold">{{ followings.length }}</p>
            </div>
          </div>

          <!-- Search -->
          <div class="space-y-2">
            <div class="flex gap-2">
              <Button
                variant="outline"
                class="bg-transparent"
                :class="searchMode === 'bojId' ? 'border-primary text-primary' : ''"
                @click="searchMode = 'bojId'"
              >
                아이디
              </Button>
              <Button
                variant="outline"
                class="bg-transparent"
                :class="searchMode === 'name' ? 'border-primary text-primary' : ''"
                @click="searchMode = 'name'"
              >
                이름
              </Button>
            </div>

            <div class="flex gap-2">
              <Input
                v-model="keyword"
                class="h-11"
                :placeholder="searchMode === 'bojId' ? '백준 아이디로 검색' : '이름으로 검색'"
                @keyup.enter="handleSearch"
              />
              <Button :disabled="searching || !keyword.trim()" @click="handleSearch" class="h-11">
                {{ searching ? '검색중...' : '검색' }}
              </Button>
            </div>

            <div v-if="searchError" class="text-sm text-red-500">
              {{ searchError }}
            </div>

            <!-- Search Results -->
            <div v-if="searchResults.length > 0" class="space-y-2 pt-2">
              <p class="text-sm text-muted-foreground">검색 결과</p>

              <div
                v-for="u in searchResults"
                :key="u.bojId"
                class="flex items-center justify-between p-3 rounded-lg border border-border hover:bg-muted transition-colors"
              >
                <div class="min-w-0">
                  <p class="font-semibold truncate">{{ u.name }}</p>
                  <p class="text-xs text-muted-foreground truncate">bojId: {{ u.bojId }}</p>
                  <p class="text-xs text-muted-foreground truncate">season: {{ u.season }}</p>
                </div>

                <div class="flex gap-2">
                  <Button
                    variant="outline"
                    class="bg-transparent"
                    :disabled="togglingBojIds.has(u.bojId)"
                    v-if="!isFollowing(u.bojId)"
                    @click="handleFollow(u.bojId)"
                  >
                    팔로우
                  </Button>

                  <Button
                    variant="outline"
                    class="bg-transparent"
                    :disabled="togglingBojIds.has(u.bojId)"
                    v-else
                    @click="handleUnfollow(u.bojId)"
                  >
                    언팔
                  </Button>
                </div>
              </div>
            </div>
          </div>

          <!-- ✅ Followers list (나를 팔로우한 사람) : 버튼 없음 -->
          <div class="space-y-2">
            <div class="flex items-center justify-between">
              <p class="font-semibold">팔로워 목록</p>
            </div>

            <div v-if="followers.length === 0" class="text-sm text-muted-foreground py-2">
              팔로워가 없습니다.
            </div>

            <div
              v-for="u in followers"
              :key="u.bojId"
              class="flex items-center justify-between p-3 rounded-lg border border-border"
            >
              <div class="min-w-0">
                <p class="font-medium truncate">{{ u.name }}</p>
                <p class="text-xs text-muted-foreground truncate">bojId: {{ u.bojId }}</p>
              </div>

              <!-- ✅ 팔로워 목록에서는 아무 버튼도 없음 -->
            </div>
          </div>

          <!-- ✅ Followings list (내가 팔로우한 사람) : 언팔로우 가능 -->
          <div class="space-y-2">
            <div class="flex items-center justify-between">
              <p class="font-semibold">팔로잉 목록</p>
            </div>

            <div v-if="followings.length === 0" class="text-sm text-muted-foreground py-2">
              팔로잉이 없습니다.
            </div>

            <div
              v-for="u in followings"
              :key="u.bojId"
              class="flex items-center justify-between p-3 rounded-lg border border-border"
            >
              <div class="min-w-0">
                <p class="font-medium truncate">{{ u.name }}</p>
                <p class="text-xs text-muted-foreground truncate">bojId: {{ u.bojId }}</p>
              </div>

              <Button
                variant="outline"
                class="bg-transparent"
                :disabled="togglingBojIds.has(u.bojId)"
                @click="handleUnfollow(u.bojId)"
              >
                언팔
              </Button>
            </div>
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
import { getScrapList } from '@/api/board'
import { getTierNumber } from '@/lib/utils'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'

import {
  searchUserByBojId,
  searchUserByName,
  followUser,
  unfollowUser,
  getFollowers,
  getFollowings,
} from '@/api/social'

const router = useRouter()

const user = ref({
  nickname: '',
  season: '',
  tier: '',
  tierNumber: null,
})

const tierSvgUrl = computed(() => {
  if (user.value.tier && user.value.tier.startsWith('http')) return user.value.tier
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
    tier: res.data.baekjoon,
    tierNumber: getTierNumber(res.data.baekjoon),
  }
}

const handleRefreshRank = async () => {
  refreshingRank.value = true
  try {
    const res = await refreshBojRank()
    const svgUrl = res?.data?.baekjoon
    if (svgUrl) user.value.tier = svgUrl
    alert('랭크가 갱신되었습니다.')
  } catch (e) {
    console.error(e)
    alert('랭크 갱신에 실패했습니다.')
  } finally {
    refreshingRank.value = false
  }
}

/** -------------------- Scraps -------------------- **/
const scrappedPosts = ref([])
const loadingScraps = ref(false)

const fetchScraps = async () => {
  loadingScraps.value = true
  try {
    const res = await getScrapList({ page: 1, size: 5 })
    scrappedPosts.value = res.data
  } catch (e) {
    console.error(e)
    alert('스크랩 목록을 불러오지 못했습니다.')
    scrappedPosts.value = []
  } finally {
    loadingScraps.value = false
  }
}

/** -------------------- Social -------------------- **/
const followers = ref([])   // ✅ 나를 팔로우한 사람 (GET /follow/from)
const followings = ref([])  // ✅ 내가 팔로우한 사람 (GET /follow/to)
const loadingSocial = ref(false)

const searchMode = ref('bojId')
const keyword = ref('')
const searching = ref(false)
const searchResults = ref([])
const searchError = ref('')

// bojId 기준으로 토글 상태 관리
const togglingBojIds = ref(new Set())

const normalizeUsers = (arr) => {
  return (arr ?? []).map((u) => ({
    bojId: u.bojId,
    name: u.name,
    season: u.season,
    baekjoon: u.baekjoon,
    withdrawn: u.withdrawn,
  }))
}

const reloadSocial = async () => {
  loadingSocial.value = true
  try {
    // ✅ 요구사항 반영:
    // - /follow/to = 내가 팔로우한 사람(팔로잉)
    // - /follow/from = 나를 팔로우한 사람(팔로워)
    const [toRes, fromRes] = await Promise.all([getFollowings(), getFollowers()])
    followings.value = normalizeUsers(toRes.data)
    followers.value = normalizeUsers(fromRes.data)
  } catch (e) {
    console.error(e)
    alert('소셜 정보를 불러오지 못했습니다.')
    followers.value = []
    followings.value = []
  } finally {
    loadingSocial.value = false
  }
}

const followingSet = computed(() => new Set(followings.value.map((u) => u.bojId)))

const isFollowing = (bojId) => {
  if (!bojId) return false
  return followingSet.value.has(bojId)
}

const handleSearch = async () => {
  const q = keyword.value.trim()
  if (!q) return

  searching.value = true
  searchError.value = ''
  searchResults.value = []

  try {
    const res = searchMode.value === 'bojId'
      ? await searchUserByBojId(q)
      : await searchUserByName(q)

    const data = Array.isArray(res.data) ? res.data : [res.data]
    searchResults.value = normalizeUsers(data)
  } catch (e) {
    console.error(e)
    searchError.value = '검색 결과를 불러오지 못했습니다.'
  } finally {
    searching.value = false
  }
}

const handleFollow = async (bojId) => {
  if (!bojId) return
  togglingBojIds.value.add(bojId)
  try {
    await followUser(bojId)
    await reloadSocial()
  } catch (e) {
    console.error(e)
    alert('팔로우에 실패했습니다.')
  } finally {
    togglingBojIds.value.delete(bojId)
  }
}

const handleUnfollow = async (bojId) => {
  if (!bojId) return
  togglingBojIds.value.add(bojId)
  try {
    // ✅ DELETE /v1/social/unfollow/{boj_id}
    await unfollowUser(bojId)
    await reloadSocial()
  } catch (e) {
    console.error(e)
    alert('언팔로우에 실패했습니다.')
  } finally {
    togglingBojIds.value.delete(bojId)
  }
}

onMounted(async () => {
  await fetchProfile()
  await fetchScraps()
  await reloadSocial()
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
