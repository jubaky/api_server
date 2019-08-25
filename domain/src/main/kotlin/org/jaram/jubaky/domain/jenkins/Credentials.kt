package org.jaram.jubaky.domain.jenkins

data class Credentials(
    val username: String,
    val password: String,
    val key: String,
    val description: String = "The credentials of username: $username",
    val scope: String = "GLOBAL"
)