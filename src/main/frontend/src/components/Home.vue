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
      <div class="row">
        <div class="col-sm-12">
          <ul class="list-group">
            <li v-for="message in messages" track-by="id" class="list-group-item">
             <span class="btn label label-pill label-default pull-right" @click="remove(message.id)"><i class="fa fa-remove"></i></span>
              {{message.content}}
            </li>
          </ul>
        </div>
      </div>
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
      if (self.content !== '') {
        axios.post('api/messages', self.content).then(self.getAll)
        self.content = ''
      }
    },
    remove: id => axios.delete('api/messages/' + id).then(self.getAll)
  },
  ready() {
    self.getAll()
  }
}
</script>

<style lang="sass" scoped>
@import "../bootstrap/_theme.scss";
h1 {
  text-align: center;
  margin: 20px;
}
.row {
  padding-left: 10px;
  padding-right: 10px;
}
.form-inline {
  margin-top: 20px;
  margin-bottom: 20px;
}
.container {
  max-width: 500px;
}
.label-pill {
  margin-top: 0.3em;
  &:hover {
    background-color: $brand-danger;
  }
}
</style>
