import com.tosmo.ktils.TimeUtils

data class Demo(val id: Long = 123, val age: Int = 123, val length:Int =123, val b: Boolean = false) {
    val name = "123"
}

fun main() {
    val time = "12:55:30"
    println(time.length)
    println(TimeUtils.parseLocalTime(time, ""))
}
