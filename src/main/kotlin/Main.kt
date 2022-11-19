import com.tosmo.ktils.TimeUtils
import com.tosmo.ktils.serializer.DateAsStringSerializer
import com.tosmo.ktils.serializer.serializerSet

fun main() {

    println(DateAsStringSerializer.pattern)
    serializerSet {
        it.datePattern = TimeUtils.DP_FULL_FORWORD_SLASH
    }
    println(DateAsStringSerializer.pattern)
}
