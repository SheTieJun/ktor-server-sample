package com.example.utils

import com.example.utils.ext.GsonExt
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Modifier
import java.lang.reflect.Type

fun getGson(): Gson = GsonBuilder()
    .excludeFieldsWithModifiers(
        Modifier.TRANSIENT,
        Modifier.STATIC
    ) // 比如我们想排除私有字段不被序列化/反序列，默认
    .registerTypeAdapter(Int::class.java, IntTypeAdapter())
    .registerTypeAdapter(Float::class.java, FloatTypeAdapter())
    .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
    .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
    .registerTypeAdapter(List::class.java, ListTypeAdapter())
    .serializeNulls() // serializeNulls支持空对象序列化
    .create()

/**
 * @author shetj
 */
object GsonKit {
    val gson: Gson by lazy { getGson() }

    /**
     * 将对象转换成json格式
     */
    @JvmStatic
    fun objectToJson(ts: Any): String? {
        return runCatching {
            gson.toJson(ts)
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 转成list中有map的
     * TODO need Test
     */
    @JvmStatic
    fun <T> jsonToListMaps(gsonString: String): List<Map<String, T>>? {
        return runCatching {
            ArrayList<Map<String, T>>().also { list ->
                JsonParser.parseString(gsonString).asJsonArray.forEach {
                    list.add(gson.fromJson(it, object : TypeToken<Map<String, Any>>() {}.type))
                }
            }
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 转成list
     * 解决泛型擦除问题
     */
    @JvmStatic
    fun <T> jsonToList(  json: String, cls: Class<T>): List<T>? {
        return runCatching<List<T>> {
            gson.fromJson(json, GsonExt.list(cls))
        }.onFailure {
            throw it
        }.getOrNull()
    }

    @JvmStatic
    fun <T> jsonToList2(  json: String, cls: Class<T>): List<T>? {
        return runCatching<List<T>> {
            gson.fromJson(json, GsonExt.list(cls))
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 转成map的
     */
    @JvmStatic
    fun jsonToMap(  gsonString: String): Map<String, Any>? {
        return runCatching<Map<String, Any>> {
            gson.fromJson(
                gsonString,
                GsonExt.map(String::class.java, Any::class.java)
            )
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 转成map的
     */
    @JvmStatic
    fun jsonToStringMap(  gsonString: String): Map<String, String>? {
        return runCatching {
            gson.fromJson<Map<String, String>>(
                gsonString,
                GsonExt.map(String::class.java, String::class.java)
            )
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 将json转换成bean对象
     */
    @JvmStatic
    fun <T> jsonToBean(  jsonStr: String, cl: Class<T>): T? {
        return runCatching<T> {
            gson.fromJson(jsonStr, cl)
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 将json转换成bean对象
     */
    @JvmStatic
    fun <T> jsonToBean(  jsonStr: String, type: Type): T? {
        return runCatching<T> {
            gson.fromJson(jsonStr, type)
        }.onFailure {
            throw it
        }.getOrNull()
    }

    /**
     * 根据key获得value值
     */
    @JvmStatic
    fun getJsonValue(  jsonStr: String, key: String): Any? {
        return kotlin.runCatching {
            gson.fromJson<Map<String, Any>>(
                jsonStr,
                object : TypeToken<Map<String, Any>>() {
                }.type
            )?.let { it[key] }
        }.onFailure {
            throw it
        }.getOrNull()
    }
}
