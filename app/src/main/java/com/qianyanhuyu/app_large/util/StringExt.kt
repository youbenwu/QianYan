package com.qianyanhuyu.app_large.util

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/21 16:44
 * @Description : String 扩展
 */

fun String.random(length: Int) : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}