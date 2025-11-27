"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Checkbox } from "@/components/ui/checkbox"
import { ThumbsUp, Star, Reply, Trash2, ChevronLeft } from "lucide-react"

export default function PostDetailPage() {
  const [liked, setLiked] = useState(false)
  const [scraped, setScraped] = useState(false)
  const [commentText, setCommentText] = useState("")
  const [anonymous, setAnonymous] = useState(false)

  const post = {
    id: 1,
    title: "Best study spots on campus - A comprehensive guide",
    author: "StudyBuddy",
    tier: "gold",
    avatar: "👤",
    date: "November 20, 2024",
    content: `I've been exploring different areas on campus to find the best spots for studying, and I want to share my findings with everyone. After weeks of testing various locations, I've narrowed it down to my top picks.

The library's third floor is incredibly quiet, especially near the back section. The natural lighting is great during the day, and there are plenty of outlets for your laptop. I usually go there in the morning for focused work sessions.

Another gem I discovered is the rooftop study area. It has a peaceful atmosphere with fresh air, and the view is quite nice. However, it's a bit chilly during colder months.

Finally, the café area near the engineering building is perfect for collaborative study sessions. The background noise is minimal, and they serve excellent coffee which keeps you energized.`,
    likes: 24,
    comments: 8,
  }

  const comments = [
    {
      id: 1,
      author: "CodeMaster",
      tier: "diamond",
      content: "Great recommendations! I especially love the rooftop area.",
      time: "1 hour ago",
      replies: 2,
    },
    {
      id: 2,
      author: "Anonymous",
      tier: null,
      content: "Thanks for sharing! Will definitely check these out.",
      time: "2 hours ago",
      replies: 0,
    },
    {
      id: 3,
      author: "CoffeeAddict",
      tier: "silver",
      content: "The café near engineering building is the best!",
      time: "3 hours ago",
      replies: 1,
      children: [
        {
          id: 31,
          author: "StudyBuddy",
          tier: "gold",
          content: "I agree! Their cappuccino is amazing.",
          time: "2 hours ago",
        },
      ],
    },
  ]

  const tierColors: Record<string, string> = {
    gold: "bg-yellow-100 text-yellow-800",
    silver: "bg-gray-300 text-gray-800",
    bronze: "bg-orange-100 text-orange-800",
    diamond: "bg-blue-100 text-blue-800",
  }

  return (
    <div className="min-h-screen bg-background p-4 pb-24">
      <div className="max-w-2xl mx-auto space-y-6">
        {/* Back Button */}
        <Button variant="ghost" className="flex items-center gap-2 -ml-2 text-primary hover:bg-primary/10">
          <ChevronLeft className="w-4 h-4" />
          Back to Board
        </Button>

        {/* Post Header */}
        <Card className="border-border">
          <CardHeader className="space-y-4">
            <h1 className="text-2xl font-bold text-foreground leading-tight">{post.title}</h1>

            {/* Author Info */}
            <div className="flex items-center gap-3 pt-2 border-t border-border/50">
              <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center text-lg">
                {post.avatar}
              </div>
              <div className="flex-1">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-sm">{post.author}</span>
                  <Badge
                    className={`${tierColors[post.tier as keyof typeof tierColors]} text-xs font-semibold rounded-full`}
                  >
                    {post.tier.charAt(0).toUpperCase() + post.tier.slice(1)}
                  </Badge>
                </div>
                <p className="text-xs text-muted-foreground">{post.date}</p>
              </div>
            </div>
          </CardHeader>
        </Card>

        {/* Post Content */}
        <Card className="border-border">
          <CardContent className="p-6">
            <p className="text-foreground leading-relaxed whitespace-pre-wrap">{post.content}</p>
          </CardContent>
        </Card>

        {/* Action Bar */}
        <div className="flex gap-2">
          <Button
            onClick={() => setLiked(!liked)}
            variant={liked ? "default" : "outline"}
            className={`flex-1 gap-2 rounded-lg h-11 ${liked ? "bg-primary text-primary-foreground" : ""}`}
          >
            <ThumbsUp className="w-4 h-4" />
            Like ({post.likes})
          </Button>
          <Button
            onClick={() => setScraped(!scraped)}
            variant={scraped ? "default" : "outline"}
            className={`flex-1 gap-2 rounded-lg h-11 ${scraped ? "bg-primary text-primary-foreground" : ""}`}
          >
            <Star className={`w-4 h-4 ${scraped ? "fill-current" : ""}`} />
            Scrap
          </Button>
        </div>

        {/* Comment Section */}
        <Card className="border-border">
          <CardHeader>
            <h2 className="font-bold text-lg">Comments ({comments.length})</h2>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* Comment Input */}
            <div className="space-y-3 p-4 bg-muted rounded-lg">
              <Input
                placeholder="Write a comment..."
                value={commentText}
                onChange={(e) => setCommentText(e.target.value)}
                className="bg-background border-input rounded-lg"
              />
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <Checkbox
                    id="anonymous"
                    checked={anonymous}
                    onCheckedChange={(checked) => setAnonymous(checked as boolean)}
                  />
                  <label htmlFor="anonymous" className="text-sm cursor-pointer text-foreground">
                    Write Anonymously
                  </label>
                </div>
                <Button
                  className="bg-primary text-primary-foreground hover:bg-primary/90 rounded-lg h-9 px-4"
                  size="sm"
                >
                  Post
                </Button>
              </div>
            </div>

            {/* Comments List */}
            <div className="space-y-4 pt-4 border-t border-border/50">
              {comments.map((comment) => (
                <div key={comment.id} className="space-y-3">
                  {/* Parent Comment */}
                  <div className="flex gap-3">
                    <div className="w-8 h-8 rounded-full bg-muted flex items-center justify-center text-sm flex-shrink-0">
                      👤
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center gap-2">
                        <span className="font-semibold text-sm">{comment.author}</span>
                        {comment.tier && (
                          <Badge className={`${tierColors[comment.tier]} text-xs font-semibold rounded-full`}>
                            {comment.tier.charAt(0).toUpperCase() + comment.tier.slice(1)}
                          </Badge>
                        )}
                      </div>
                      <p className="text-sm text-muted-foreground mt-1">{comment.content}</p>
                      <div className="flex items-center gap-4 mt-2">
                        <button className="text-xs text-muted-foreground hover:text-primary flex items-center gap-1">
                          <Reply className="w-3 h-3" />
                          Reply
                        </button>
                        <span className="text-xs text-muted-foreground">{comment.time}</span>
                      </div>
                    </div>
                    <Button variant="ghost" size="sm" className="p-2 text-destructive hover:bg-destructive/10 h-auto">
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>

                  {/* Child Comments */}
                  {comment.children &&
                    comment.children.map((child) => (
                      <div
                        key={child.id}
                        className="ml-11 space-y-2 p-3 bg-muted/50 rounded-lg border-l-2 border-primary/30"
                      >
                        <div className="flex items-center gap-2">
                          <span className="font-semibold text-sm">{child.author}</span>
                          <Badge className={`${tierColors[child.tier]} text-xs font-semibold rounded-full`}>
                            {child.tier.charAt(0).toUpperCase() + child.tier.slice(1)}
                          </Badge>
                        </div>
                        <p className="text-sm text-foreground">{child.content}</p>
                        <span className="text-xs text-muted-foreground">{child.time}</span>
                      </div>
                    ))}
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
