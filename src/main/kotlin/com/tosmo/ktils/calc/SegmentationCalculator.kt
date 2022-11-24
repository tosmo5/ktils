package com.tosmo.ktils.calc

import java.math.BigDecimal

/**
 * 分段计算器
 *
 * @author Thomas Miao
 */
class SegmentationCalculator {

    /**
     * 每段
     */
    data class Section(
        val start: BigDecimal,
        val end: BigDecimal,
        val percentage: BigDecimal
    )

    private val mSections = mutableListOf<Section>()

    /**
     * 分段信息
     */
    val setions
        get() = mSections.toList()

    /**
     * 添加
     */
    fun add(sectionEnd: Number, percentage: Number) {
        val sectionEndD = BigDecimal(sectionEnd.toString())
        val percentageD = BigDecimal(percentage.toString())

        val lastRange = mSections.lastOrNull()
        val rangeStart = lastRange?.end ?: BigDecimal.ZERO
        require(sectionEndD > rangeStart)
        require(percentageD >= BigDecimal.ZERO)
        mSections.add(Section(rangeStart, sectionEndD, percentageD))
    }

    /**
     * 批量添加
     */
    fun addAll(vararg pair: Pair<Number, Number>) {
        pair.forEach {
            add(it.first, it.second)
        }
    }

    /**
     * 计算
     */
    fun calc(number: Number): BigDecimal {
        val n = BigDecimal(number.toString())
        return mSections.sumOf {
            if (n >= it.end) {
                (it.end - it.start) * it.percentage / BigDecimal(100)
            } else if (n >= it.start) {
                (n - it.start) * it.percentage / BigDecimal(100)
            } else 0.toBigDecimal()
        }
    }

    fun clear() {
        mSections.clear()
    }
}
