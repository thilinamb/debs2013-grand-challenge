var http = require('http')
var fs = require('fs')

http.createServer(function (req, res) {
  res.writeHead(200, {'content-type': 'text/plain'});
  res.end('Hello Node!\n');
}).listen(8124);

console.log('Server running on 8124');
