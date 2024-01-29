package com.example.plugins

import com.example.db.UserService
import com.example.key.Constant
import com.example.key.DbConstant
import com.example.key.RedisConstant
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

fun Application.configKoin(dev:Boolean) {
    // Install Koin
    install(Koin) {
        modules(dbModule(dev))
        modules(getHttpClientModule())
        modules(getRedisModule(dev))
    }

}

private fun getRedisModule(developmentMode: Boolean): Module {
    return module {
        single {
            val config = JedisPoolConfig()
            config.maxTotal = 10 // 最大连接数
            config.maxIdle = 5 // 最大空闲连接数
            config.minIdle = 1 // 最小空闲连接数
            JedisPool(config, if (developmentMode) Constant.DEBUG_IP else RedisConstant.REDIS_IP, RedisConstant.REDIS_PORT).apply {
                // 使用您的实际密码进行身份验证
                resource.use {
                    it.auth(RedisConstant.REDIS_PASSWORD)
                }
            }
        }
    }
}

private fun getHttpClientModule(): Module {
    return module {
        single {
            HttpClient(CIO)
        }
    }
}

/**
 * Db module
 * 数据库处理
 */
private fun dbModule(developmentMode: Boolean): Module {
    return module() {
        single {
            Database.connect(
                url = if (developmentMode) DbConstant.DEBUG_DB_URL else DbConstant.DB_URL,
                user = DbConstant.DB_USER,
                driver = DbConstant.JDBC_DRIVER,
                password = DbConstant.DB_PASSWORD
            )
        }

        single {
            UserService(get())
        }
    }
}