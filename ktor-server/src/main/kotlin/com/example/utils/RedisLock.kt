package com.example.utils

import redis.clients.jedis.Jedis
import redis.clients.jedis.params.SetParams

class RedisLock(private val jedis: Jedis) {
    fun lock(key: String, value: String, expireTime: Long): Boolean {
        val params = SetParams()
        params.nx().px(expireTime)
        val result: String = jedis.set(key, value, params)
        return "OK" == result
    }

    fun unlock(key: String, value: String) {
        if (value == jedis.get(key)) {
            jedis.del(key)
        }
    }
}

