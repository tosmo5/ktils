package com.tosmo.ktils

/**
 * 内容参数替换类
 *
 * @author Thomas Miao
 */
object ParamReplacer {

    /**
     * 替换的参数，[toString]输出[text]
     */
    data class Param(val key: Any) {
        /**
         * 匹配的正则表达式
         */
        val regex = Regex("\\$\\{${key}}")

        /**
         * 文本中的参数形式
         */
        val text = "\${${key}}"

        override fun toString(): String {
            return text
        }
    }

    /**
     * 用[params]替换[content]中所有的匹配的参数，返回替换了参数后的文本内容
     *
     * @author Thomas Miao
     *
     * <pre><code>
     * val args = mutableMapOf("id" to 123, "name" to "张三", "age" to 12)
     *
     * val idParam = ParamReplacer.Param("id")
     * val nameParam = ParamReplacer.Param("name")
     * val ageParam = ParamReplacer.Param("age")
     *
     * val content = "编号为${idParam}的${nameParam}，年龄为${ageParam}"
     *
     * val lastContent = ParamReplacer.replace(content, args)
     *
     * output: 编号为123的张三，年龄为12
     * <code><pre>
     *
     */
    fun replace(content: String, params: Map<*, *>): String {
        var lastContent = content
        params.forEach { (key, value) ->
            key?.let {
                lastContent = lastContent.replace(
                    Param(key).regex, value.toString()
                )
            }
        }
        return lastContent
    }
}