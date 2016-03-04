import EventBus from 'vertx3-eventbus-client'

// const eventbus = new EventBus('http://localhost:8080/eventbus')
const eventbus = new EventBus('/eventbus')

const handlers = []

eventbus.handle = function(url, bodyHandler) {
  handlers.push({url, bodyHandler})
}

eventbus.onopen = function() {
  handlers.forEach(handler => eventbus.registerHandler(handler.url, function(err, msg) {
    if (err) {
      console.log('SockJS/EventBus error: ', err)
    } else {
      handler.bodyHandler(JSON.parse(msg.body))
    }
  }))
}

export default eventbus
