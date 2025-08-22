package HigerOrderFunctions

fun main() {
    // Create a list of numbers from 1 to 10
    val nums = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    // Use the 'all' function to check a condition on every element of the list
    val is5Present = nums.all { it ->
        // 'it' represents each element of the list one by one
        it == 5   // Check if the current element is equal to 5
        // 'all' will return true ONLY if this condition is true for EVERY element
        // Otherwise it returns false
    }

    // Print the result
    println(is5Present) // prints false, because not all numbers in the list are 5
}
