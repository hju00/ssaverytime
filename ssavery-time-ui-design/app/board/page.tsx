"use client"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Heart, MessageCircle, Search, Plus } from "lucide-react"

export default function BoardPage() {
  const [filterHot, setFilterHot] = useState(false)
  const [searchQuery, setSearchQuery] = useState("")

  const tierColors: Record<string, string> = {
    gold: "bg-yellow-100 text-yellow-800",
    silver: "bg-gray-300 text-gray-800",
    bronze: "bg-orange-100 text-orange-800",
    diamond: "bg-blue-100 text-blue-800",
  }

  const posts = [
    {
      id: 1,
      title: "Best study spots on campus",
      preview: "I found these amazing quiet places perfect for focusing during exam season...",
      author: "StudyBuddy",
      tier: "gold",
      time: "2 hours ago",
      likes: 24,
      comments: 8,
      hot: true,
    },
    {
      id: 2,
      title: "Algorithm tips and tricks",
      preview: "Here are some efficient coding patterns I learned while solving LeetCode problems...",
      author: "CodeMaster",
      tier: "diamond",
      time: "4 hours ago",
      likes: 156,
      comments: 42,
      hot: true,
    },
    {
      id: 3,
      title: "Coffee recommendations nearby",
      preview: "A list of the best coffee shops within walking distance from our campus...",
      author: "CoffeeAddict",
      tier: "silver",
      time: "6 hours ago",
      likes: 45,
      comments: 12,
      hot: false,
    },
    {
      id: 4,
      title: "Internship experience sharing",
      preview: "Just finished my summer internship at a tech company. Here are my learnings...",
      author: "TechEnthusiast",
      tier: "gold",
      time: "1 day ago",
      likes: 89,
      comments: 28,
      hot: true,
    },
  ]

  const filteredPosts = filterHot ? posts.filter((p) => p.hot) : posts

  return (
    <div className="min-h-screen bg-background p-4">
      <div className="max-w-3xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex flex-col gap-4">
          <div className="flex items-center justify-between">
            <h1 className="text-3xl font-bold text-foreground">Free Board</h1>
            <Button className="gap-2 bg-primary text-primary-foreground hover:bg-primary/90 rounded-lg">
              <Plus className="w-4 h-4" />
              <span className="hidden sm:inline">Write Post</span>
            </Button>
          </div>

          {/* Search and Filter */}
          <div className="flex gap-2 flex-col sm:flex-row">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search posts..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10 rounded-lg bg-background border-input h-10"
              />
            </div>
            <Button
              onClick={() => setFilterHot(!filterHot)}
              variant={filterHot ? "default" : "outline"}
              className={`rounded-lg h-10 ${filterHot ? "bg-primary text-primary-foreground" : ""}`}
            >
              {filterHot ? "Hot Posts" : "All Posts"}
            </Button>
          </div>
        </div>

        {/* Posts List */}
        <div className="space-y-3">
          {filteredPosts.map((post) => (
            <Card key={post.id} className="border-border hover:shadow-md transition-shadow cursor-pointer">
              <CardContent className="p-4 space-y-3">
                {/* Title */}
                <h2 className="font-bold text-lg text-foreground line-clamp-2">{post.title}</h2>

                {/* Preview */}
                <p className="text-sm text-muted-foreground line-clamp-2">{post.preview}</p>

                {/* Metadata Row */}
                <div className="flex items-center justify-between pt-2 border-t border-border/50">
                  <div className="flex items-center gap-2">
                    <span className="text-xs font-medium text-foreground">{post.author}</span>
                    <Badge className={`${tierColors[post.tier]} text-xs font-semibold rounded-full`}>
                      {post.tier.charAt(0).toUpperCase() + post.tier.slice(1)}
                    </Badge>
                    <span className="text-xs text-muted-foreground">{post.time}</span>
                  </div>

                  {/* Engagement Icons */}
                  <div className="flex items-center gap-4">
                    <div className="flex items-center gap-1 text-xs text-muted-foreground">
                      <Heart className="w-4 h-4" />
                      <span>{post.likes}</span>
                    </div>
                    <div className="flex items-center gap-1 text-xs text-muted-foreground">
                      <MessageCircle className="w-4 h-4" />
                      <span>{post.comments}</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  )
}
