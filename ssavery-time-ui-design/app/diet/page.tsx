"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Progress } from "@/components/ui/progress"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Checkbox } from "@/components/ui/checkbox"
import { ChevronLeft, ChevronRight, Pencil, Utensils, Trash2 } from "lucide-react"

export default function DietPage() {
  const [selectedDate, setSelectedDate] = useState(new Date(2024, 10, 20))
  const [showLunchModal, setShowLunchModal] = useState(false)
  const [selectedMenus, setSelectedMenus] = useState<string[]>([])

  const weekDates = Array.from({ length: 7 }, (_, i) => {
    const d = new Date(selectedDate)
    d.setDate(d.getDate() - d.getDay() + i)
    return d
  })

  const foods = [
    { time: "08:30", name: "Oatmeal with berries", calories: 320 },
    { time: "12:15", name: "Grilled chicken salad", calories: 450 },
    { time: "15:45", name: "Greek yogurt", calories: 150 },
    { time: "19:00", name: "Salmon with rice", calories: 580 },
  ]

  const menus = [
    { id: "sodamsang", name: "Sodamsang", items: ["Bibimbap", "Kimchi stew", "Steamed vegetables"] },
    { id: "gourmet", name: "The Gourmet", items: ["Pasta carbonara", "Caesar salad", "Tiramisu"] },
    { id: "bbq", name: "BBQ House", items: ["Grilled beef", "Charcoal grilled chicken", "Korean side dishes"] },
  ]

  const handleMenuSelect = (menuId: string) => {
    setSelectedMenus((prev) => (prev.includes(menuId) ? prev.filter((id) => id !== menuId) : [...prev, menuId]))
  }

  const totalCalories = foods.reduce((sum, food) => sum + food.calories, 0)
  const calorieGoal = 2000
  const caloriePercentage = (totalCalories / calorieGoal) * 100

  return (
    <div className="min-h-screen bg-background p-4 pb-24">
      <div className="max-w-2xl mx-auto space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <h1 className="text-3xl font-bold text-foreground">My Diet</h1>
          <p className="text-muted-foreground">Track your daily nutrition intake</p>
        </div>

        {/* Weekly Calendar */}
        <Card className="border-border">
          <CardContent className="p-4">
            <div className="flex items-center justify-between mb-4">
              <Button variant="ghost" size="sm" className="p-2">
                <ChevronLeft className="w-4 h-4" />
              </Button>
              <span className="font-semibold text-sm">
                {selectedDate.toLocaleDateString("en-US", { month: "short", year: "numeric" })}
              </span>
              <Button variant="ghost" size="sm" className="p-2">
                <ChevronRight className="w-4 h-4" />
              </Button>
            </div>
            <div className="grid grid-cols-7 gap-2">
              {weekDates.map((date, i) => (
                <button
                  key={i}
                  className={`p-3 rounded-lg text-center transition-all text-sm ${
                    date.toDateString() === selectedDate.toDateString()
                      ? "bg-primary text-primary-foreground font-semibold"
                      : "bg-muted text-foreground hover:bg-input"
                  }`}
                  onClick={() => setSelectedDate(date)}
                >
                  <div className="text-xs mb-1">{["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"][date.getDay()]}</div>
                  <div>{date.getDate()}</div>
                </button>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Daily Intake Summary */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle className="text-lg">Daily Intake vs Goal</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-2">
              <div className="flex items-center justify-between text-sm">
                <span className="text-muted-foreground">Total Calories</span>
                <span className="font-semibold">
                  {totalCalories} / {calorieGoal} kcal
                </span>
              </div>
              <Progress value={caloriePercentage} className="h-2 rounded-full" />
            </div>
            <div className="bg-primary/10 border border-primary/20 rounded-lg p-4">
              <p className="text-sm text-foreground">
                <span className="font-semibold">AI Analysis:</span> Good protein balance today! Consider increasing your
                vegetable intake for better micronutrients.
              </p>
            </div>
          </CardContent>
        </Card>

        {/* Foods Timeline */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle className="text-lg">Today's Meals</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            {foods.length === 0 ? (
              <p className="text-center text-muted-foreground text-sm py-8">No meals logged yet</p>
            ) : (
              foods.map((food, i) => (
                <div
                  key={i}
                  className="flex items-center justify-between p-3 bg-muted rounded-lg hover:bg-input transition-colors"
                >
                  <div className="flex-1">
                    <div className="flex items-center gap-3">
                      <span className="font-mono text-sm font-semibold text-primary w-12">{food.time}</span>
                      <div>
                        <p className="font-medium text-sm text-foreground">{food.name}</p>
                        <p className="text-xs text-muted-foreground">{food.calories} kcal</p>
                      </div>
                    </div>
                  </div>
                  <Button variant="ghost" size="sm" className="p-2 text-destructive hover:bg-destructive/10">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              ))
            )}
          </CardContent>
        </Card>

        {/* Floating Action Buttons */}
        <div className="fixed bottom-6 right-6 flex flex-col gap-3 sm:flex-row sm:bottom-6 sm:left-1/2 sm:transform sm:-translate-x-1/2">
          <Button
            onClick={() => setShowLunchModal(true)}
            className="rounded-full shadow-lg h-14 gap-2 bg-primary text-primary-foreground hover:bg-primary/90 sm:rounded-lg sm:h-11 sm:w-auto"
          >
            <Utensils className="w-5 h-5" />
            <span className="hidden sm:inline">Import SSAFY Lunch</span>
          </Button>
          <Button variant="secondary" className="rounded-full shadow-lg h-14 gap-2 sm:rounded-lg sm:h-11 sm:w-auto">
            <Pencil className="w-5 h-5" />
            <span className="hidden sm:inline">Add Manually</span>
          </Button>
        </div>

        {/* Lunch Modal */}
        <Dialog open={showLunchModal} onOpenChange={setShowLunchModal}>
          <DialogContent className="rounded-2xl">
            <DialogHeader>
              <DialogTitle>Import SSAFY Lunch</DialogTitle>
              <DialogDescription>Select meals from the cafeteria to add them to your diet</DialogDescription>
            </DialogHeader>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
              {menus.map((menu) => (
                <div
                  key={menu.id}
                  className="border-2 border-input rounded-lg p-4 cursor-pointer transition-all hover:border-primary hover:bg-primary/5"
                  onClick={() => handleMenuSelect(menu.id)}
                >
                  <div className="flex items-start gap-3 mb-3">
                    <Checkbox
                      checked={selectedMenus.includes(menu.id)}
                      onCheckedChange={() => handleMenuSelect(menu.id)}
                    />
                    <div>
                      <h3 className="font-semibold text-sm">{menu.name}</h3>
                    </div>
                  </div>
                  <ul className="space-y-1 text-xs text-muted-foreground">
                    {menu.items.map((item, i) => (
                      <li key={i}>• {item}</li>
                    ))}
                  </ul>
                </div>
              ))}
            </div>
            <div className="flex gap-2 mt-6 justify-end">
              <Button variant="outline" onClick={() => setShowLunchModal(false)}>
                Cancel
              </Button>
              <Button
                className="bg-primary text-primary-foreground hover:bg-primary/90"
                onClick={() => {
                  console.log("Selected menus:", selectedMenus)
                  setShowLunchModal(false)
                }}
              >
                Add Selected
              </Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>
    </div>
  )
}
