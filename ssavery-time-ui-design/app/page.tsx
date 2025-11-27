"use client"

import type React from "react"

import { useState } from "react"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { LogIn, UserPlus } from "lucide-react"

export default function AuthPage() {
  const [loginData, setLoginData] = useState({ username: "", password: "" })
  const [signupData, setSignupData] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    nickname: "",
    campus: "",
    baekjoonId: "",
  })

  const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setLoginData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSignupChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setSignupData((prev) => ({ ...prev, [name]: value }))
  }

  const handleCampusChange = (value: string) => {
    setSignupData((prev) => ({ ...prev, campus: value }))
  }

  const handleLogin = () => {
    console.log("Login:", loginData)
  }

  const handleSignup = () => {
    console.log("Signup:", signupData)
  }

  const handleVerifyBaekjoon = () => {
    console.log("Verifying Baekjoon ID:", signupData.baekjoonId)
  }

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-2">
          <div className="flex items-center justify-center gap-2 mb-4">
            <div className="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-primary-foreground font-bold">S</span>
            </div>
            <CardTitle className="text-2xl">SsaveryTime</CardTitle>
          </div>
          <CardDescription className="text-center">SSAFY Community Service</CardDescription>
        </CardHeader>

        <CardContent>
          <Tabs defaultValue="login" className="w-full">
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="login" className="flex gap-2">
                <LogIn className="w-4 h-4" />
                <span className="hidden sm:inline">Login</span>
              </TabsTrigger>
              <TabsTrigger value="signup" className="flex gap-2">
                <UserPlus className="w-4 h-4" />
                <span className="hidden sm:inline">Signup</span>
              </TabsTrigger>
            </TabsList>

            <TabsContent value="login" className="space-y-4 mt-6">
              <div className="space-y-2">
                <Label htmlFor="login-username" className="text-sm font-medium">
                  Username
                </Label>
                <Input
                  id="login-username"
                  name="username"
                  placeholder="Enter your username"
                  value={loginData.username}
                  onChange={handleLoginChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="login-password" className="text-sm font-medium">
                  Password
                </Label>
                <Input
                  id="login-password"
                  name="password"
                  type="password"
                  placeholder="Enter your password"
                  value={loginData.password}
                  onChange={handleLoginChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <Button
                onClick={handleLogin}
                className="w-full h-11 rounded-lg bg-primary text-primary-foreground font-medium mt-6 hover:bg-primary/90"
              >
                Login
              </Button>
            </TabsContent>

            <TabsContent value="signup" className="space-y-4 mt-6">
              <div className="space-y-2">
                <Label htmlFor="signup-username" className="text-sm font-medium">
                  Username
                </Label>
                <Input
                  id="signup-username"
                  name="username"
                  placeholder="Choose a username"
                  value={signupData.username}
                  onChange={handleSignupChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="signup-password" className="text-sm font-medium">
                  Password
                </Label>
                <Input
                  id="signup-password"
                  name="password"
                  type="password"
                  placeholder="Create a password"
                  value={signupData.password}
                  onChange={handleSignupChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="confirm-password" className="text-sm font-medium">
                  Confirm Password
                </Label>
                <Input
                  id="confirm-password"
                  name="confirmPassword"
                  type="password"
                  placeholder="Confirm your password"
                  value={signupData.confirmPassword}
                  onChange={handleSignupChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="nickname" className="text-sm font-medium">
                  Nickname
                </Label>
                <Input
                  id="nickname"
                  name="nickname"
                  placeholder="Display name"
                  value={signupData.nickname}
                  onChange={handleSignupChange}
                  className="rounded-lg border-input bg-background h-11"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="campus" className="text-sm font-medium">
                  Select Campus
                </Label>
                <Select value={signupData.campus} onValueChange={handleCampusChange}>
                  <SelectTrigger id="campus" className="rounded-lg border-input bg-background h-11">
                    <SelectValue placeholder="Choose your campus" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="gwangju">Gwangju</SelectItem>
                    <SelectItem value="seoul">Seoul</SelectItem>
                    <SelectItem value="daejeon">Daejeon</SelectItem>
                    <SelectItem value="gumi">Gumi</SelectItem>
                    <SelectItem value="buil">Buil</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="baekjoon" className="text-sm font-medium">
                  Baekjoon ID
                </Label>
                <div className="flex gap-2">
                  <Input
                    id="baekjoon"
                    name="baekjoonId"
                    placeholder="Your Baekjoon ID"
                    value={signupData.baekjoonId}
                    onChange={handleSignupChange}
                    className="rounded-lg border-input bg-background h-11 flex-1"
                  />
                  <Button
                    onClick={handleVerifyBaekjoon}
                    variant="outline"
                    className="rounded-lg border-input h-11 px-4 bg-transparent"
                  >
                    Verify
                  </Button>
                </div>
              </div>

              <Button
                onClick={handleSignup}
                className="w-full h-11 rounded-lg bg-primary text-primary-foreground font-medium mt-6 hover:bg-primary/90"
              >
                Create Account
              </Button>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  )
}
