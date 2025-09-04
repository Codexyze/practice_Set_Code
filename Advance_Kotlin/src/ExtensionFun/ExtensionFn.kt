package ExtensionFun
fun String.mySize():Int{
    return this.length
}
fun main(){
   val myString = "1234567"
   val value = myString.mySize()
   println(value)

}

