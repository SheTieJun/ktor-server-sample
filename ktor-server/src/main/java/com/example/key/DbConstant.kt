package com.example.key

object DbConstant {
    const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    const val DB_URL = "jdbc:mysql://${Constant.WLS_IP}:3306/test?characterEncoding=utf-8&characterSetResults=utf8&autoReconnect=true&useSSL=false&allowMultiQueries=true"
    const val DB_USER = "root"
    const val DB_PASSWORD = "123456"
    const val DEBUG_DB_URL = "jdbc:mysql://${Constant.DEBUG_IP}:3306/test?characterEncoding=utf-8&characterSetResults=utf8&autoReconnect=true&useSSL=false&allowMultiQueries=true"
}