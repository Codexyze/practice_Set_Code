/***********************************************************************
 *  JavaScript Basics - Notes for Android Developers (Kotlin mindset)
 *
 *  Topics covered:
 *  1. Variables (var / let / const)
 *  2. Arrays
 *  3. forEach
 *  4. map
 *  5. filter
 *  6. Functions
 *  7. Objects
 *  8. async / await (Networking example)
 *
 *  Rule of this file:
 *  - EVERYTHING is logged
 *  - LOTS of comments
 *  - Think like Kotlin equivalents
 ***********************************************************************/


/***********************************************************************
 * 1️⃣ VARIABLES
 ***********************************************************************/

// var → old style (function scoped) ❌ avoid in modern JS
var oldVariable = 10;
console.log("var example:", oldVariable);

// let → mutable variable (like Kotlin `var`)
let count = 5;
count = 10;
console.log("let example (mutable):", count);

// const → immutable variable (like Kotlin `val`)
const PI = 3.14;
// PI = 4.0 ❌ Not allowed
console.log("const example (immutable):", PI);


/***********************************************************************
 * 2️⃣ ARRAYS
 ***********************************************************************/

// Array declaration
// Kotlin equivalent: val arr = listOf(1,2,3,4,5,6)
let numbers = [1, 2, 3, 4, 5, 6];

console.log("Array:", numbers);
console.log("Array length:", numbers.length);
console.log("First element:", numbers[0]);
console.log("Last element:", numbers[numbers.length - 1]);


/***********************************************************************
 * 3️⃣ FUNCTIONS
 ***********************************************************************/

// Normal function
function sum(a, b) {
    return a + b;
}

console.log("Sum function result:", sum(2, 3));

// Arrow function (like Kotlin lambda)
const multiply = (a, b) => {
    return a * b;
};

console.log("Arrow function result:", multiply(4, 5));


/***********************************************************************
 * 4️⃣ forEach (SIDE EFFECT LOOP)
 ***********************************************************************/

/*
 * forEach is used when:
 * - You want to LOOP
 * - You DO NOT want to return a new array
 *
 * Kotlin equivalent:
 * numbers.forEach { println(it) }
 */

console.log("forEach example:");

numbers.forEach((element, index) => {
    let result = sum(element, element);
    console.log(
        `Index: ${index}, Element: ${element}, Sum with itself: ${result}`
    );
});


/***********************************************************************
 * 5️⃣ map (TRANSFORMATION)
 ***********************************************************************/

/*
 * map is used when:
 * - You want to TRANSFORM data
 * - You WANT a NEW array
 *
 * Kotlin equivalent:
 * val doubled = numbers.map { it * 2 }
 */

let doubledNumbers = numbers.map((element) => {
    return element * 2;
});

console.log("Original array:", numbers);
console.log("Mapped (doubled) array:", doubledNumbers);


/***********************************************************************
 * 6️⃣ filter (CONDITION BASED SELECTION)
 ***********************************************************************/

/*
 * filter is used when:
 * - You want ONLY SOME elements
 *
 * Kotlin equivalent:
 * val even = numbers.filter { it % 2 == 0 }
 */

let evenNumbers = numbers.filter((element) => {
    return element % 2 === 0;
});

console.log("Filtered (even numbers):", evenNumbers);


/***********************************************************************
 * 7️⃣ OBJECTS
 ***********************************************************************/

/*
 * JS Object ≈ Kotlin data class (loosely)
 */

let user = {
    name: "Ram",
    age: 20,
    isActive: true
};

console.log("User object:", user);
console.log("User name:", user.name);
console.log("User age:", user.age);

// Adding new property dynamically
user.city = "Mumbai";
console.log("Updated user object:", user);


/***********************************************************************
 * 8️⃣ ARRAY OF OBJECTS (VERY COMMON IN APIs)
 ***********************************************************************/

let users = [
    { id: 1, name: "Ram", age: 20 },
    { id: 2, name: "Shyam", age: 17 },
    { id: 3, name: "Aman", age: 25 }
];

console.log("Users array:", users);

// Filter adults
let adults = users.filter(user => user.age >= 18);
console.log("Adult users:", adults);

// Map names
let userNames = users.map(user => user.name);
console.log("User names:", userNames);


/***********************************************************************
 * 9️⃣ ASYNC / AWAIT (MOST IMPORTANT)
 ***********************************************************************/

/*
 * async / await ≈ Kotlin coroutines (suspend functions)
 *
 * - async function ALWAYS returns a Promise
 * - await waits for async work to finish
 */

async function getData() {

    console.log("Fetching data...");

    // fetch() returns a Promise
    let response = await fetch("https://jsonplaceholder.typicode.com/posts");

    console.log("Raw response:", response);

    // Convert response to JSON
    let result = await response.json();

    console.log("Parsed JSON result:", result);

    // Log first item for clarity
    console.log("First post:", result[0]);
}

// Calling async function
getData();


/***********************************************************************
 * 🔟 SUMMARY (MENTAL MODEL)
 ***********************************************************************/

/*
 * var      → avoid
 * let      → mutable (Kotlin var)
 * const    → immutable (Kotlin val)
 *
 * forEach  → loop only (no return)
 * map      → transform → NEW array
 * filter   → select → NEW array
 *
 * object   → key-value structure
 *
 * async    → suspend function
 * await    → wait for result (non-blocking)
 */
