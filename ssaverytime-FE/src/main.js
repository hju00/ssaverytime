import { createApp } from 'vue'
import './assets/index.css' // Import Tailwind CSS
import App from './App.vue'
import router from './router' // Import the router
import { createI18n } from 'vue-i18n'
import en from './locales/en.json'
import ko from './locales/ko.json'

const i18n = createI18n({
  locale: 'ko', // set locale
  fallbackLocale: 'en', // set fallback locale
  messages: {
    en,
    ko
  },
})

const app = createApp(App)
app.use(router)
app.use(i18n)
app.mount('#app')
