package com.tosmo.ktils

import java.time.Instant


class IdWorker(var workerId: Long = 0, var datacenterId: Long = 0, var sequence: Long = 0) {
    
    private val timeEpoch = Instant.parse("2022-08-31T00:00:00Z").toEpochMilli()
    
    //长度为5位
    private val workerIdBits = 5
    private val datacenterIdBits = 5
    
    //最大值
    private val maxWorkerId = -1L xor (-1L shl workerIdBits)
    private val maxDatacenterId = -1L xor (-1L shl datacenterIdBits)
    
    //序列号id长度
    private val sequenceBits = 12
    
    //序列号最大值
    private val sequenceMask = -1L xor (-1L shl sequenceBits)
    
    //工作id需要左移的位数，12位
    private val workerIdShift = sequenceBits
    
    //数据id需要左移位数 12+5=17位
    private val datacenterIdShift = sequenceBits + workerIdBits
    
    //时间戳需要左移位数 12+5+5=22位
    private val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits
    
    //上次时间戳，初始值为负数
    private var lastTimestamp = -1L
    
    private val timeGen = { System.currentTimeMillis() }
    
    init {
        require(!(workerId > maxWorkerId || workerId < 0)) {
            "worker Id can't be greater than $maxWorkerId or less than 0"
        }
        require(!(datacenterId > maxDatacenterId || datacenterId < 0)) {
            "datacenter Id can't be greater than $maxDatacenterId or less than 0"
        }
    }
    
    //下一个ID生成算法
    @Synchronized
    fun nextId(): Long {
        var timestamp = timeGen()
        
        //获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        require(timestamp >= lastTimestamp) {
            "Clock moved backwards. Refusing to generate id for ${lastTimestamp - timestamp} seconds"
        }
        
        //获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (lastTimestamp == timestamp) {
            sequence = sequence + 1 and sequenceMask
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0
        }
        
        //将上次时间戳值刷新
        lastTimestamp = timestamp
        
        return ((timestamp - timeEpoch) shl timestampLeftShift) or
                (datacenterId shl datacenterIdShift) or
                (workerId shl workerIdShift) or
                sequence
    }
    
    //获取时间戳，并与上次时间戳比较
    private fun tilNextMillis(lastTimestamp: Long): Long =
        sequence { while (true) yield((timeGen())) }.first { it > lastTimestamp }
}
