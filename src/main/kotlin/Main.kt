import com.tosmo.ktils.ParamReplacer

fun main() {

    val args = mutableMapOf(
        "id" to 123, "name" to "张三", "age" to 12
    )

    val idParam = ParamReplacer.Param("id")
    val nameParam = ParamReplacer.Param("name")
    val ageParam = ParamReplacer.Param("age")

    val content = "编号为${idParam}的${nameParam}，年龄为${ageParam}"

    println(content)
    val last = ParamReplacer.replace(content, args)
    println(last)
}
