package org.jaram.jubaky.ext

import org.koin.core.scope.Scope

fun Scope.getInt(key: String): Int? {
    return getKoin().getProperty<Int>(key)
}

fun Scope.getInt(key: String, defaultValue: Int): Int {
    return getKoin().getProperty<Int>(key, defaultValue)
}

fun Scope.getBoolean(key: String): Boolean? {
    return getKoin().getProperty<String>(key)?.toBoolean()
}

fun Scope.getBoolean(key: String, defaultValue: Boolean): Boolean {
    return getKoin().getProperty<String>(key)?.toBoolean() ?: defaultValue
}

fun Scope.getString(key: String): String? {
    return getKoin().getProperty(key)
}

fun Scope.getString(key: String, defaultValue: String): String {
    return getKoin().getProperty(key, defaultValue)
}