package org.jaram.jubaky.domain.jenkins

data class JobConfig(
    val description: String = "Description of Job",
    val keepDependencies: String = "false",
    val buildArgumentList: List<BuildArgument>,
    val githubUrl: String,
    val githubCredentials: String,
    val githubBranch: String,
    val beforeCommand: String,
    val afterCommand: String,
    val dockerArgument: DockerArgument
)