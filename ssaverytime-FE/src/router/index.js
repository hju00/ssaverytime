import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import Board from '../views/Board.vue'
import Diet from '../views/Diet.vue'
import Post from '../views/Post.vue'
import PostWrite from '../views/PostWrite.vue'
import Profile from '../views/Profile.vue'
import ProfileEdit from '../views/ProfileEdit.vue'
import Admin from '../views/Admin.vue'
import ScrapList from '../views/ScrapList.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/signup', name: 'Signup', component: Signup },
  { path: '/admin', name: 'Admin', component: Admin },
  { path: '/board', name: 'Board', component: Board },
  { path: '/board/write', name: 'PostWrite', component: PostWrite },
  { path: '/diet', name: 'Diet', component: Diet },
  { path: '/post/:id', name: 'Post', component: Post },

  { path: '/profile', name: 'Profile', component: Profile },
  { path: '/profile/edit', name: 'ProfileEdit', component: ProfileEdit },

  { path: '/scraps', name: 'ScrapList', component: ScrapList }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
