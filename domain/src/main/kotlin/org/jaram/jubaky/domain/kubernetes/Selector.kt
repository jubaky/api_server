package org.jaram.jubaky.domain.kubernetes

data class Selector(
    val matchExpressions: List<SelectorRequirements>?,
    val matchLabels: Map<String, String>?
) {
    data class SelectorRequirements(
        val key: String?,
        val operator: String?,
        val values: List<String>?
    )
}