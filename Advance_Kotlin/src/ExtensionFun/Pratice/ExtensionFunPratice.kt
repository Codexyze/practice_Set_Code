package ExtensionFun.Pratice

fun Int.givePlusValue():Int{
    return this.plus(1)
}

fun main(){
    println("Input a val")
    val num:Int = readln().toInt()
    print(num.givePlusValue())

}