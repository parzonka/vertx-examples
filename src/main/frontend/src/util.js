// removes each item in vue data satisfying predicate
function remove(data, predicate) {
  data.forEach(function(item) {
    if (predicate(item)) {
      data.$remove(item)
    }
  })
}

export { remove }
