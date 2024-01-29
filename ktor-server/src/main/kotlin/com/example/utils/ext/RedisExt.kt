package com.example.utils.ext

import com.example.utils.RedisLock
import redis.clients.jedis.Jedis

fun Jedis.lock(key: String, value: String, expireTime: Long = 1000 * 60 * 60 * 24, block: () -> Unit) {
    use {
        val lock = RedisLock(this)
        if (lock.lock(key, value, expireTime)) {
            try {
                block()
            } finally {
                lock.unlock(key, value)
            }
        }
    }
}