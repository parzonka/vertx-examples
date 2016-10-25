<template>
  <div class="users" v-on:keyup.enter="post">
    <div class="container">
      <h1>Message Wall</h1>
      <div id="socket-info" class="text-muted">Connected users: {{connectionCount}}</div>
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
             <span class="btn btn-sm label label-pill label-default pull-right" @click="remove(message.id)">
                <i class="fa fa-remove remove-cross"></i>
              </span>
              {{message.content}}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/babel">
import axios from 'axios'
import eventbus from './../eventbus'
import { remove } from './../util'
let self
export default {
  data () {
    self = this
    return {
      content: '',
      messages: [],
      connectionCount: 1
    }
  },
  methods: {
    getAll: () => axios.get('api/messages').then(res => self.messages = res.data),
    post: () => {
      if (self.content !== '') {
        axios.post('api/messages', self.content)
        self.content = ''
      }
    },
    remove: id => axios.delete('api/messages/' + id)
  },
  ready() {
    // when the component is loaded 1. the current state is fetched from the server
    // 2. push create and delete handlers are registered
    self.getAll()
    eventbus.handle('messages/created', message => self.messages.push(message))
    eventbus.handle('messages/deleted', message => remove(self.messages, storedMessage => message.id === storedMessage.id))
    eventbus.handle('connections', connections => self.connectionCount = connections.count)
  }
}
</script>

<style lang="scss" scoped>
@import "../bootstrap/_theme.scss";
h1 {
  text-align: center;
  margin-top: 20px;
}

#socket-info {
  text-align: center;
  margin-bottom: 20px;
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
  border-radius: 10px;
  &:hover {
    background-color: $brand-danger;
    .remove-cross {
      color: #fff;
    }
  }
}
.remove-cross {
  color: #aaa;
}
</style>

