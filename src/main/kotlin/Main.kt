import com.tosmo.ktils.Reflecter
import com.tosmo.ktils.calc.SegmentationCalculator
import java.math.BigDecimal

data class Demo(
    val name: String,
    val age: Int,
    val weight: Float
)

fun main() {

    val args = buildMap {
        put("name", "tosmo")
        put("age", 16.9)
        put("weight", 120)
    }
    val demo = Reflecter.createBean(Demo::class, args)
    println(demo)
}