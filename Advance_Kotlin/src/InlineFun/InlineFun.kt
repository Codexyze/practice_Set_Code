package InlineFun

inline fun message(crossinline message:()->Unit){
    message()

}

fun main(){
    message {
        println("Hello message 1")
        return@message
    }
    message {
        println("Hello message 2")
    }
}