import com.tosmo.ktils.calc.SegmentationCalculator
import java.math.BigDecimal

fun main() {

    val mCalc = SegmentationCalculator()
    mCalc.add(36000, 3)
    mCalc.add(144000, 10)
    mCalc.add(300000, 20)

    val r = mCalc.calc(300000)
    println(r.equals(43080))
}