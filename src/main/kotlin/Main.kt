import com.tosmo.ktils.TimeUtils

fun main() {

    val dateStr = "2022-11-18"

    val date = TimeUtils.parseDate(dateStr, TimeUtils.DP_YEAR_MONTH_DAY)

    println(date)
    println(TimeUtils.format(date, TimeUtils.DP_YEAR_MONTH_DAY))
}
