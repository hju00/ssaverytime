import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Auth from '../views/Auth.vue'
import Board from '../views/Board.vue'
import Diet from '../views/Diet.vue'
import Post from '../views/Post.vue'
import Profile from '../views/Profile.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Auth
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Auth
  },
  {
    path: '/board',
    name: 'Board',
    component: Board
  },
  {
    path: '/diet',
    name: 'Diet',
    component: Diet
  },
  {
    path: '/post/:id',
    name: 'Post',
    component: Post
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router