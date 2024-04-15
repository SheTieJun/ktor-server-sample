package com.example.utils.ext

import kotlinx.html.HTML
import kotlinx.html.head
import kotlinx.html.meta

/**
 * 兼容手机端的viewport
 */
fun HTML.viewport() {
    head {
        meta {
            content = "width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
            name = "viewport"
        }
    }
}