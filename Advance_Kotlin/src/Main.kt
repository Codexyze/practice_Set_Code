fun main() {
  val nums = listOf(1,2,3,4,5,6,7,8,9,10)
  val is5Present = nums.any { it->
      it==5
  }
    println(is5Present)
}

