// Import Node.js built-in HTTP module
// This module is used to create a basic web server
const http = require('node:http');


// Import third-party npm package "one-liner-joke"
// This package provides random short jokes
// (It must be installed using: npm install one-liner-joke)
var oneLinerJoke = require('one-liner-joke');


// Call getRandomJoke() once
// IMPORTANT:
// - This returns an OBJECT, not a string
// - Example object structure:
//   {
//      body: "Joke text here",
//      tags: ["funny"]
//   }
var getRandomJoke = oneLinerJoke.getRandomJoke();


// Create an HTTP server
// This callback function runs every time a request comes to the server
// req  -> request object (data coming from client/browser)
// res  -> response object (data we send back to client/browser)
const server = http.createServer((req, res) => {

    // Send response to the client and end the response
    // We send ONLY the "body" property because:
    // - res.end() accepts string or buffer
    // - getRandomJoke.body is a string
    res.end(getRandomJoke.body);

});


// Start the server and bind it to port 3000
// After this line, server becomes accessible at:
// http://localhost:3000
server.listen(3000);
