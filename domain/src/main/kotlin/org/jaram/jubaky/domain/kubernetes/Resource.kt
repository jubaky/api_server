package org.jaram.jubaky.domain.kubernetes

import java.math.BigDecimal

data class Resource(
    val limits: Map<String, Quantity>?,
    val requests: Map<String, Quantity>?
) {
    data class Quantity(
        val number: BigDecimal?,
        val format: Int?
    )
}