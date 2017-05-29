var restify = require('restify');
var mongoose = require('mongoose')
var bunyan = require('bunyan');
var log = bunyan.createLogger({name: 'OpinionPoll-API'});


// Connect to the database
mongoose.set('debug', true);
mongoose.connect('mongodb://localhost/pole_db');


function respond(req, res, next) {
  res.send('What the fuck' + req.params.name +'\n');
  next();
}



// This is orginal content


// Create the basic server
var server = restify.createServer({
  name: 'OpinionPoll-API',
  log: log,
});

server.get('/hello/:name', respond);
server.head('/hello/:name', respond);

// Helper for parsing request body and parsing request parameters
server.use(restify.bodyParser());
server.use(restify.queryParser());

// If the Node process ends, close the Mongoose connection
process.on('SIGINT', function() {
  mongoose.connection.close(function () {
    console.log('Mongoose disconnected on app termination');
    process.exit(0);
  });
});

// Setup routing for the server
require('./routes')(server);

// Specify what the server is listening to.
server.listen(8080, "127.0.0.1");
console.log('Server running at http://127.0.0.1:8080/');

