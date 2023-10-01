import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

//Vuesax
import Vuesax from 'vuesax-alpha'
//default mode
import 'vuesax-alpha/theme-chalk/index.css'
// dark mode
import 'vuesax-alpha/theme-chalk/dark/css-vars.css'

//pinia持久化
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

//Boxicons
import 'boxicons'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(Vuesax)

app.mount('#app')
