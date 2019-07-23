package org.jaram.jubaky.protocol

open class BaseResponse<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T? = null,
    val timestamp: Long = System.currentTimeMillis()
)