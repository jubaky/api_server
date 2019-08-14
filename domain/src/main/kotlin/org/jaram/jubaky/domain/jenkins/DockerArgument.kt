package org.jaram.jubaky.domain.jenkins

data class DockerArgument(
    val dockerUsername: String,
    val dockerPassword: String,
    val imageUsername: String,
    val imageName: String,
    val imageVersion: String
)