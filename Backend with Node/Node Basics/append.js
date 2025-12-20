// Import the `error` function from Node's console module
// This is optional, but shows how destructuring import works
const { error } = require('node:console');

// Import Node.js File System module
// Used for reading and writing files
const fs = require('node:fs');

// appendFile() appends data to a file
// If "hey.txt" does not exist, Node.js will create it automatically
fs.appendFile(
    "hey.txt",          // File name (relative to current working directory)
    "dangerous cat",    // Text/data to be appended to the file
    error => {          // Callback function (runs after file operation completes)

        // If error is present, file operation failed
        if (error) {
            console.error("Error");
        }
        // If error is null/undefined, file operation succeeded
        else {
            console.log("Done sucessfully");
        }
    }
);
