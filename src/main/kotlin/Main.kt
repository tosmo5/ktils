import com.tosmo.ktils.TimeUtils
import java.time.LocalTime

fun main() {
    println(TimeUtils.format(LocalTime.MIN))
    println(TimeUtils.format(LocalTime.MAX))
}
