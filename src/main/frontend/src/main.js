import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './components/App'
// import 'bootstrap/dist/css/bootstrap.css'
import 'font-awesome-webpack'
// import './theme/_theme.scss'
// import './theme/app.scss'
import './bootstrap/_theme.scss'

// import { useVueStrap } from './config/vuestrap'
import { setupRoutes } from './routes'

// addons
Vue.use(VueRouter)

// router
var router = new VueRouter({
  // virtual api flag
  history: false
})
setupRoutes(router)

// useVueStrap()
Vue.config.debug = true

router.start(App, '#app')
