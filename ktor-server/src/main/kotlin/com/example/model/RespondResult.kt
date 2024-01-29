package com.example.model

data class RespondResult(val code: Int, val msg: String, val data: Any?) {
    companion object {
        fun success(data: Any? = null, msg: String = "success") = RespondResult(200, msg, data)
        fun error(code: Int, msg: String) = RespondResult(code, msg, null)
    }
}