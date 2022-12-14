import com.tosmo.ktils.serializer.DateAsStringSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.*

internal fun main() {
    val dateStr = Json.encodeToString(DateAsStringSerializer, Date.from(Instant.now()))

    println(dateStr)
    println("2022-12-12")
}