package HigherOrderFunctions

fun main() {
    // A list of numbers
    val nums = listOf(1, 2, 3, 4, 5, 6, 7, 5, 7, 6, 9)

    // Use 'filter' to create a new list containing only numbers > 5
    val numsGreaterThan5 = nums.filter { it ->
        it > 5  // 'it' represents each element. If condition is true, element is included in new list
    }

    // Print the filtered list
    println(numsGreaterThan5)  // Output: [6, 7, 7, 6, 9]
}
