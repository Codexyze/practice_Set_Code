package ForLoop

fun main(){
    val array = arrayOf(1,2,3,4,5,6,7,8,9,10)
    for (i in 'a'..'z'){
        println(i)
    }
    println("---------------")

    for (i in 10 downTo 5 step 2){
        println(i)
    }
    println("---------------")

    var sum =0
    for (item in array){
        sum = sum + item
    }
    println("Sum of array is $sum")


}