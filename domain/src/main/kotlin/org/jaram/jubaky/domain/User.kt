package org.jaram.jubaky.domain

data class User(
    val emailId: String,
    val password: String,
    val name: String,
    val groupName: String
)