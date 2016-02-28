var express = require('express')
var webpack = require('webpack')
var config = require('./webpack.dev.conf')
var proxy = require('http-proxy-middleware')

var app = express()
var compiler = webpack(config)

// map calls to /api/* to http://localhost:8080/api/*
app.use(proxy('/api', {target: 'http://localhost:8080'}))

// handle fallback for HTML5 history API
app.use(require('connect-history-api-fallback')())

// serve webpack bundle output
app.use(require('webpack-dev-middleware')(compiler, {
  publicPath: config.output.publicPath,
  stats: {
    colors: true,
    chunks: false
  }
}))

// enable hot-reload and state-preserving
// compilation error display
app.use(require('webpack-hot-middleware')(compiler))

app.listen(3000, '0.0.0.0', function (err) {
  if (err) {
    console.log(err)
    return
  }
  console.log('Listening at http://localhost:3000')
})
