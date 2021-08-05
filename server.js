var http = require('http');
http.createServer(function(req,res){
        res.writeHead(200, { 'Content-Type': 'text/plain' });
        res.end('Hello folks!');
}).listen(8070);
console.log('Server started on localhost:8070; press Ctrl-C to terminate...!');
© 2021 GitHub, Inc.
