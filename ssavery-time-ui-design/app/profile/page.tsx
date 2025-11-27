"use client"

import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Progress } from "@/components/ui/progress"
import { FileText, MessageSquare, Bookmark, Settings, ChevronRight } from "lucide-react"

export default function ProfilePage() {
  const user = {
    nickname: "StudyBuddy",
    campus: "Seoul",
    avatar: "👤",
    tier: "Gold 1",
    currentRating: 1850,
    nextTierThreshold: 2000,
  }

  const menuItems = [
    { icon: FileText, label: "My Posts", count: 24 },
    { icon: MessageSquare, label: "My Comments", count: 87 },
    { icon: Bookmark, label: "Scrapped Posts", count: 12 },
    { icon: Settings, label: "Edit Profile", count: null },
  ]

  const scrappedPosts = [
    {
      id: 1,
      title: "Top 10 Algorithm tips for beginners",
      author: "CodeMaster",
    },
    {
      id: 2,
      title: "Best resources for learning System Design",
      author: "DesignPro",
    },
    {
      id: 3,
      title: "How to prepare for coding interviews",
      author: "InterviewExpert",
    },
  ]

  return (
    <div className="min-h-screen bg-background p-4 pb-12">
      <div className="max-w-2xl mx-auto space-y-6">
        {/* Profile Card */}
        <Card className="border-border text-center">
          <CardContent className="p-8 space-y-4">
            <div className="w-20 h-20 rounded-full bg-primary flex items-center justify-center text-5xl mx-auto">
              {user.avatar}
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground">{user.nickname}</h1>
              <p className="text-sm text-muted-foreground">{user.campus} Campus</p>
            </div>
          </CardContent>
        </Card>

        {/* Rank Section */}
        <Card className="border-border">
          <CardHeader>
            <h2 className="font-bold text-lg">Baekjoon Tier</h2>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-lg bg-yellow-100 flex items-center justify-center font-bold text-yellow-800">
                  AU
                </div>
                <div>
                  <p className="font-semibold text-lg">{user.tier}</p>
                  <p className="text-xs text-muted-foreground">Algorithm Rank</p>
                </div>
              </div>
              <span className="text-sm font-semibold text-primary">{user.currentRating}</span>
            </div>
            <div className="space-y-2">
              <div className="flex items-center justify-between text-sm">
                <span className="text-muted-foreground">Progress to next tier</span>
                <span className="font-semibold">
                  {Math.round((user.currentRating / user.nextTierThreshold) * 100)}%
                </span>
              </div>
              <Progress value={(user.currentRating / user.nextTierThreshold) * 100} className="h-2 rounded-full" />
              <p className="text-xs text-muted-foreground text-right">
                {user.nextTierThreshold - user.currentRating} points until next tier
              </p>
            </div>
          </CardContent>
        </Card>

        {/* Menu Grid */}
        <div className="grid grid-cols-2 gap-3">
          {menuItems.map((item, i) => {
            const Icon = item.icon
            return (
              <Button
                key={i}
                variant="outline"
                className="h-24 flex flex-col items-start justify-between p-4 rounded-lg border-border hover:bg-muted hover:border-primary group bg-transparent"
              >
                <Icon className="w-6 h-6 text-primary group-hover:text-primary/80" />
                <div className="text-left">
                  <p className="font-semibold text-sm text-foreground">{item.label}</p>
                  {item.count !== null && <p className="text-xs text-muted-foreground">{item.count}</p>}
                </div>
              </Button>
            )
          })}
        </div>

        {/* Scrapped Posts Preview */}
        <Card className="border-border">
          <CardHeader>
            <h2 className="font-bold text-lg">Recently Scrapped</h2>
          </CardHeader>
          <CardContent className="space-y-3">
            {scrappedPosts.map((post) => (
              <div
                key={post.id}
                className="flex items-start justify-between p-3 hover:bg-muted rounded-lg transition-colors cursor-pointer group"
              >
                <div className="flex-1">
                  <h3 className="font-medium text-sm text-foreground line-clamp-2 group-hover:text-primary">
                    {post.title}
                  </h3>
                  <p className="text-xs text-muted-foreground mt-1">by {post.author}</p>
                </div>
                <ChevronRight className="w-4 h-4 text-muted-foreground flex-shrink-0 mt-1" />
              </div>
            ))}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
