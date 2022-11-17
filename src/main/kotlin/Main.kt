import com.tosmo.ktils.Reflecter

data class Demo(val id: Long = 123, val age: Int = 123, val length:Int =123, val b: Boolean = false) {
    val name = "123"
}

fun main() {
    val demo = Demo()
    val map = Reflecter.mapParams(demo)
    println(map)
}
