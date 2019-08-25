package org.jaram.jubaky.domain.kubernetes

data class Status(
    val apiVersion: String?,
    val code: Int?,
    val details: StatusDetails?,
    val kind: String?,
    val message: String?,
    val metadata: ListMeta?,
    val reason: String?,
    val status: String?
) {
    data class StatusDetails(
        val causes: List<StatusCause>?,
        val group: String?,
        val kind: String?,
        val name: String?,
        val retryAfterSeconds: Int?,
        val uid: String?
    )

    data class ListMeta(
        val _continue: String?,
        val resourceVersion: String?,
        val selfLink: String?
    )

    data class StatusCause(
        val field: String?,
        val message: String?,
        val reason: String?
    )
}