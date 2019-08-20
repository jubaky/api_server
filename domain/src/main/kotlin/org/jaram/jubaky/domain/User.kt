package org.jaram.jubaky.domain

data class User(
    val emailId: String,
    val password: ByteArray,
    val name: String,
    val groupName: String
)