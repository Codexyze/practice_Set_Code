package HigerOrderFunctions

fun main(){
    message {
        println("hello")
    }
}
fun message(message:()->Unit){
    message.invoke()
}