package org.jaram.jubaky.domain

data class Application(
    val id: Int,
    val name: String,
    val gitRepositoryUrl: String,
    val url: String?
)