import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './components/App'
import { setupRoutes } from './routes'
import './bootstrap'

Vue.config.debug = true

Vue.use(VueRouter)
const router = new VueRouter({
  // when false, #! urls are used
  history: false
})
setupRoutes(router)

router.start(App, '#app')
