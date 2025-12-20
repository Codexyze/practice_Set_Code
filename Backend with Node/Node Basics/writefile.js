// Import the `error` function from Node's console module
// Demonstrates destructuring import (not mandatory for writeFile)
const { error } = require('node:console');

// Import Node.js File System module
// Used to perform file read/write operations
const fs = require('node:fs');

// writeFile() writes data to a file
// ⚠️ IMPORTANT: It OVERWRITES the file if it already exists
// If the file does NOT exist, Node.js creates it
fs.writeFile(
    "hey.txt",                // File name (relative to current directory)
    "This is my data ..",      // Data to be written into the file
    error => {                // Callback function executed after operation

        // If error exists → file write failed
        if (error) {
            console.error("Error");
        }
        // If no error → file write succeeded
        else {
            console.log("Done sucessfully");
        }
    }
);
