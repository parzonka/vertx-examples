<template>
  <div class="users">
    <div class="container">
      <h1>Messages</h1>
      <ul class="list-group">
        <li v-for="message in messages" class="list-group-item">{{message.content}}</li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
let self
export default {
  data () {
    self = this
    return {
      messages: []
    }
  },
  methods: {
    getAll: () => axios.get('api/messages').then(res => self.messages = res.data)
  },
  ready() {
    self.getAll()
  }
}
</script>

<style scoped>
h1 {
  text-align: center;
  margin: 20px;
}
.container {
  max-width: 300px;
}
</style>
