<template>
  <div class="min-h-screen bg-background p-4 pb-24">
    <div class="max-w-2xl mx-auto space-y-6">
      <!-- Header -->
      <div class="space-y-2">
        <h1 class="text-3xl font-bold text-foreground">{{ $t('diet.title') }}</h1>
        <p class="text-muted-foreground">{{ $t('diet.description') }}</p>
      </div>

      <!-- Weekly Calendar -->
      <Card class="border-border">
        <CardContent class="p-4">
          <div class="flex items-center justify-between mb-4">
            <Button variant="ghost" size="sm" class="p-2" @click="changeWeek(-1)">
              <ChevronLeftIcon class="w-4 h-4" />
            </Button>
            <span class="font-semibold text-sm">
              {{ selectedDate.toLocaleDateString("en-US", { month: "short", year: "numeric" }) }}
            </span>
            <Button variant="ghost" size="sm" class="p-2" @click="changeWeek(1)">
              <ChevronRightIcon class="w-4 h-4" />
            </Button>
          </div>
          <div class="grid grid-cols-7 gap-2">
            <button
              v-for="(date, i) in weekDates"
              :key="i"
              :class="[
                'p-3 rounded-lg text-center transition-all text-sm',
                date.toDateString() === selectedDate.toDateString()
                  ? 'bg-primary text-primary-foreground font-semibold'
                  : 'bg-muted text-foreground hover:bg-input',
              ]"
              @click="setSelectedDate(date)"
            >
              <div class="text-xs mb-1">{{ ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'][date.getDay()] }}</div>
              <div>{{ date.getDate() }}</div>
            </button>
          </div>
        </CardContent>
      </Card>

      <!-- Daily Intake Summary -->
      <Card class="border-border">
        <CardHeader>
          <CardTitle class="text-lg">{{ $t('diet.daily_intake_vs_goal') }}</CardTitle>
        </CardHeader>
        <CardContent class="space-y-4">
          <div class="space-y-2">
            <div class="flex items-center justify-between text-sm">
              <span class="text-muted-foreground">{{ $t('diet.total_calories') }}</span>
              <span class="font-semibold">
                {{ totalCalories }} / {{ calorieGoal }} kcal
              </span>
            </div>
            <Progress :model-value="caloriePercentage" class="h-2 rounded-full" />
          </div>
          <div class="bg-primary/10 border border-primary/20 rounded-lg p-4">
            <p class="text-sm text-foreground">
              <span class="font-semibold">{{ $t('diet.ai_analysis_prefix') }}</span> {{ $t('diet.ai_analysis_message') }}
            </p>
          </div>
        </CardContent>
      </Card>

      <!-- Foods Timeline -->
      <Card class="border-border">
        <CardHeader>
          <CardTitle class="text-lg">{{ $t('diet.today_meals') }}</CardTitle>
        </CardHeader>
        <CardContent class="space-y-3">
          <p v-if="foods.length === 0" class="text-center text-muted-foreground text-sm py-8">{{ $t('diet.no_meals_logged') }}</p>
          <div
            v-else
            v-for="(food, i) in foods"
            :key="i"
            class="flex items-center justify-between p-3 bg-muted rounded-lg hover:bg-input transition-colors"
          >
            <div class="flex-1">
              <div class="flex items-center gap-3">
                <span class="font-mono text-sm font-semibold text-primary w-12">{{ food.time }}</span>
                <div>
                  <p class="font-medium text-sm text-foreground">{{ food.name }}</p>
                  <p class="text-xs text-muted-foreground">{{ food.calories }} kcal</p>
                </div>
              </div>
            </div>
            <Button variant="ghost" size="sm" class="p-2 text-destructive hover:bg-destructive/10">
              <Trash2Icon class="w-4 h-4" />
            </Button>
          </div>
        </CardContent>
      </Card>

      <!-- Floating Action Buttons -->
      <div class="fixed bottom-6 right-6 flex flex-col gap-3 sm:flex-row sm:bottom-6 sm:left-1/2 sm:transform sm:-translate-x-1/2">
        <Button
          @click="showLunchModal = true"
          class="rounded-full shadow-lg h-14 gap-2 bg-primary text-primary-foreground hover:bg-primary/90 sm:rounded-lg sm:h-11 sm:w-auto"
        >
          <UtensilsIcon class="w-5 h-5" />
          <span class="hidden sm:inline">{{ $t('diet.import_ssafy_lunch') }}</span>
        </Button>
        <Button variant="secondary" class="rounded-full shadow-lg h-14 gap-2 sm:rounded-lg sm:h-11 sm:w-auto">
          <PencilIcon class="w-5 h-5" />
          <span class="hidden sm:inline">{{ $t('diet.add_manually') }}</span>
        </Button>
      </div>

      <!-- Lunch Modal -->
      <Dialog :open="showLunchModal" @update:open="showLunchModal = $event">
        <DialogContent class="rounded-2xl">
          <DialogHeader>
            <DialogTitle>{{ $t('diet.modal_title') }}</DialogTitle>
            <DialogDescription>{{ $t('diet.modal_description') }}</DialogDescription>
          </DialogHeader>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
            <div
              v-for="menu in menus"
              :key="menu.id"
              :class="[
                'border-2 border-input rounded-lg p-4 cursor-pointer transition-all',
                selectedMenus.includes(menu.id) ? 'border-primary bg-primary/5' : 'hover:border-primary hover:bg-primary/5',
              ]"
              @click="handleMenuSelect(menu.id)"
            >
              <div class="flex items-start gap-3 mb-3">
                <Checkbox
                  :checked="selectedMenus.includes(menu.id)"
                  @update:checked="handleMenuSelect(menu.id)"
                />
                <div>
                  <h3 class="font-semibold text-sm">{{ menu.name }}</h3>
                </div>
              </div>
              <ul class="space-y-1 text-xs text-muted-foreground">
                <li v-for="(item, i) in menu.items" :key="i"> • {{ item }}</li>
              </ul>
            </div>
          </div>
          <div class="flex gap-2 mt-6 justify-end">
            <Button variant="outline" @click="showLunchModal = false">
              {{ $t('diet.cancel_button') }}
            </Button>
            <Button
              class="bg-primary text-primary-foreground hover:bg-primary/90"
              @click="addSelectedMenus"
            >
              {{ $t('diet.add_selected_button') }}
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Progress } from '@/components/ui/progress'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Checkbox } from '@/components/ui/checkbox'
import {
  ChevronLeft as ChevronLeftIcon,
  ChevronRight as ChevronRightIcon,
  Pencil as PencilIcon,
  Utensils as UtensilsIcon,
  Trash2 as Trash2Icon,
} from 'lucide-vue-next'

const selectedDate = ref(new Date(2024, 10, 20)) // Month is 0-indexed in JS
const showLunchModal = ref(false)
const selectedMenus = ref([])

const weekDates = computed(() => {
  const dates = []
  const startOfWeek = new Date(selectedDate.value)
  startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay()) // Go to Sunday

  for (let i = 0; i < 7; i++) {
    const d = new Date(startOfWeek)
    d.setDate(startOfWeek.getDate() + i)
    dates.push(d)
  }
  return dates
})

const changeWeek = (direction) => {
  selectedDate.value = new Date(selectedDate.value.setDate(selectedDate.value.getDate() + (direction * 7)))
}

const setSelectedDate = (date) => {
  selectedDate.value = date
}

const foods = ref([
  { time: '08:30', name: 'Oatmeal with berries', calories: 320 },
  { time: '12:15', name: 'Grilled chicken salad', calories: 450 },
  { time: '15:45', name: 'Greek yogurt', calories: 150 },
  { time: '19:00', name: 'Salmon with rice', calories: 580 },
])

const menus = ref([
  { id: 'sodamsang', name: 'Sodamsang', items: ['Bibimbap', 'Kimchi stew', 'Steamed vegetables'] },
  { id: 'gourmet', name: 'The Gourmet', items: ['Pasta carbonara', 'Caesar salad', 'Tiramisu'] },
  { id: 'bbq', name: 'BBQ House', items: ['Grilled beef', 'Charcoal grilled chicken', 'Korean side dishes'] },
])

const handleMenuSelect = (menuId) => {
  if (selectedMenus.value.includes(menuId)) {
    selectedMenus.value = selectedMenus.value.filter((id) => id !== menuId)
  } else {
    selectedMenus.value = [...selectedMenus.value, menuId]
  }
}

const totalCalories = computed(() => foods.value.reduce((sum, food) => sum + food.calories, 0))
const calorieGoal = 2000
const caloriePercentage = computed(() => (totalCalories.value / calorieGoal) * 100)

const addSelectedMenus = () => {
  console.log('Selected menus:', selectedMenus.value)
  showLunchModal.value = false
  // Logic to add selected menus to foods array would go here
}
</script>

<style scoped>
/* Scoped styles for Diet.vue */
</style>