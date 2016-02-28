
import Home from './components/Home'

function setupRoutes(router) {

  router.map({
    '/home': {
      name: 'home',
      component: Home
    }
  })

  router.redirect({
    '*': '/home'
  })

}

export { setupRoutes }
