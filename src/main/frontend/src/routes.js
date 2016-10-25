import Home from './components/Home'

function setupRoutes(router) {
  router.map({
    '/home': {
      component: Home
    }
  })
  router.redirect({
    '*': '/home'
  })
}

export { setupRoutes }
