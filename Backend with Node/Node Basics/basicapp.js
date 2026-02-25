// Import Express framework
// Express makes building HTTP servers easier than raw Node http module
const express = require('express');


// Import third-party npm package for random jokes
// Provides short one-liner jokes
var oneLinerJoke = require('one-liner-joke');


// Create an Express application instance
// This `app` object represents your server
const app = express();


// -------------------------
// ROUTE: HOME PAGE (/)
// -------------------------
// app.get() handles HTTP GET requests
// '/' means root URL → http://localhost:3000/
app.get('/', (req, res) => {

  // Get a random joke
  // This returns an object, not a string
  var getRandomJoke = oneLinerJoke.getRandomJoke();

  // Send ONLY the joke text (body) as response
  // res.send() automatically converts string to HTTP response
  res.send(getRandomJoke.body);
});


// -------------------------
// ROUTE: PROFILE PAGE (/profile)
// -------------------------
// When user visits http://localhost:3000/profile
app.get('/profile', (req, res) => {

  // Send plain text response
  res.send("Profile loaded....");
});


// -------------------------
// START THE SERVER
// -------------------------
// app.listen() starts the server
// Port 3000 is the entry point for requests
app.listen(3000, () => {

  // Callback runs once server starts successfully
  console.log('Server is running on http://localhost:3000');
});
