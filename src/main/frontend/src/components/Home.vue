<template>
  <div class="users">
    <div class="container">
      <h1>Messages</h1>
      <div class="form-group row">
        <div class="col-sm-9">
          <input class="form-control" placeholder="Message" v-model="content" >
        </div>
        <div class="col-sm-3">
          <button class="btn btn-primary btn-block" @click="post">Send</button>
        </div>
      </div>
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
      content: '',
      messages: []
    }
  },
  methods: {
    getAll: () => axios.get('api/messages').then(res => self.messages = res.data),
    post: () => {
      axios.post('api/messages', {id: 1, content: self.content}).then(self.getAll)
      self.content = ''
    }
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
.form-inline {
  margin-top: 20px;
  margin-bottom: 20px;
}

.container {
  max-width: 500px;
}
</style>
