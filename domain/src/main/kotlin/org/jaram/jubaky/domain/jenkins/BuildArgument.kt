package org.jaram.jubaky.domain.jenkins

data class BuildArgument(
    val name: String?,
    val defaultValue: String? = "",
    val value: String? = "",
    val description: String? = "Argument : $name\nDefault value: $defaultValue",
    val trim: String = "false"
) {
    fun toMap() {

    }
}