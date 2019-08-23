package org.jaram.jubaky.presenter.router

import io.ktor.application.call
import io.ktor.routing.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.Credentials
import org.jaram.jubaky.domain.jenkins.DockerArgument
import org.jaram.jubaky.domain.jenkins.JobConfig
import org.jaram.jubaky.domain.session.UserSession
import org.jaram.jubaky.presenter.ext.*
import org.jaram.jubaky.service.ApplicationService
import org.jaram.jubaky.service.BuildService
import org.jaram.jubaky.service.DeployService
import org.jaram.jubaky.service.UserService

fun Route.app(
    applicationService: ApplicationService,
    buildService: BuildService,
    deployService: DeployService,
    userService: UserService
) {
    get {
        response(applicationService.getApplicationList())
    }

    route("/credentials") {
        post("/") {
            val session = call.sessions.get<UserSession>()
            val userId = userService.getUserId(session?.emailId)

            val credentials = Credentials(
                username = bodyParam("username"),
                password = bodyParam("password"),
                key = bodyParam("key"),
                description = bodyParam("description", "Credentials description"),
                scope = bodyParam("scope", "GLOBAL")
            )


            response(
                applicationService.createCredentials(userId, credentials)
            )
        }

        get("/") {
            val session = call.sessions.get<UserSession>()
            val userId = userService.getUserId(session?.emailId)

            response(
                applicationService.getCredentialList(userId)
            )
        }

        delete("/{key}") {
            val key = pathParam("key")

            response(
                applicationService.deleteCredentials(key)
            )
        }
    }

    route("/{applicationId}") {
        get {
            response(applicationService.getApplicationInfo(pathParam("applicationId").toInt()))
        }

        route("/build") {
            get {
                val applicationId = pathParam("applicationId").toInt()
                val topSize = queryParamSafe("top")?.toIntOrNull() ?: 10

                val session = call.sessions.get<UserSession>()
                val userId = userService.getUserId(session?.emailId)

                response(
                    buildService.getRecentBuildList(userId, applicationId, topSize)
                )
            }

            get("/{buildId}") {
                response(buildService.getBuildInfo(pathParam("buildId").toInt()))
            }
        }

        route("/deploy") {
            get {
                val topSize = queryParamSafe("top")?.toIntOrNull() ?: 10
                val namespace = queryParamSafe("namespace")

                val session = call.sessions.get<UserSession>()
                val userId = userService.getUserId(session?.emailId)

                val buildId = 1
                val applicationId = 1

                response(
                    deployService.getRecentDeployList(userId, buildId, applicationId, topSize, namespace)
                )
            }

            get("/{deployId}") {
                response(deployService.getDeployInfo(pathParam("deployId").toInt()))
            }

            post {
                val buildId = bodyParam("build_id").toInt()
                val namespace = bodyParam("namespace")
                val yaml = bodyParam("yaml")

                response(deployService.runDeploy(buildId, namespace, yaml))
            }
        }

        route("/branch") {
            get {
                response(applicationService.getBranchList(pathParam("applicationId").toInt()))
            }

            get("/build") {
                val session = call.sessions.get<UserSession>()
                val userId = userService.getUserId(session?.emailId)

                response(
                    buildService.getRecentBuildList(
                        userId,
                        pathParam("applicationId").toInt(),
                        queryParamSafe("top")?.toIntOrNull() ?: 10,
                        queryParam("branch_name")
                    )
                )
            }

            post("/") {
                val applicationId = pathParam("applicationId").toInt()
                val branchName = queryParam("branch_name")

                // Docker image arguments
                val dockerArgument = DockerArgument(
                    dockerUsername = bodyParam("docker_username"),
                    dockerPassword = bodyParam("docker_password"),
                    imageUsername = bodyParam("image_username"),
                    imageName = bodyParam("image_name"),
                    imageVersion = bodyParam("image_version")
                )

                // Docker build arguments
                val buildArgNameList = bodyParamListSafe("docker_argument_name")
                val buildArgDefaultValueList = bodyParamListSafe("docker_argument_default_value")
                val buildArgDescriptionList = bodyParamListSafe("docker_argument_description")

                val buildArgumentList = mutableListOf<BuildArgument>()

                if (buildArgNameList.size == buildArgDefaultValueList.size && buildArgNameList.size == buildArgDescriptionList.size) {
                    for (i in 0 until buildArgNameList.size) {
                        val buildArgument = BuildArgument(
                            name = buildArgNameList[i],
                            defaultValue = buildArgDefaultValueList[i],
                            description = buildArgDescriptionList[i]
                        )

                        buildArgumentList.add(buildArgument)
                    }
                }

                val configData = JobConfig(
                    description = bodyParam("description", "Job description"),
                    keepDependencies = bodyParam("keep_dependencies", "false"),
                    buildArgumentList = buildArgumentList,
                    githubUrl = bodyParam("github_url"),
                    githubCredentials = bodyParam("github_credentials"),
                    githubBranch = branchName,
                    beforeCommand = bodyParam("before_command", ""),
                    afterCommand = bodyParam("after_command", ""),
                    dockerArgument = dockerArgument
                )

                response(
                    buildService.createJob(
                        applicationId = applicationId,
                        configData = configData
                    )
                )
            }

            post("/build") {
                // Docker build arguments
                val buildArgNameList = bodyParamListSafe("docker_argument_name")
                val buildArgValueList = bodyParamListSafe("docker_argument_value")
                val buildArgDescriptionList = bodyParamListSafe("docker_argument_description")

                val buildArgumentList = mutableListOf<BuildArgument>()

                if (buildArgNameList.size == buildArgValueList.size && buildArgNameList.size == buildArgDescriptionList.size) {
                    for (i in 0 until buildArgNameList.size) {
                        val buildArgument = BuildArgument(
                            name = buildArgNameList[i],
                            defaultValue = buildArgValueList[i],
                            description = buildArgDescriptionList[i]
                        )

                        buildArgumentList.add(buildArgument)
                    }
                }

                response(
                    buildService.runBuild(
                        pathParam("applicationId").toInt(),
                        queryParam("branch_name"),
                        buildArgumentList
                    )
                )
            }

            get("/deploy") {
                response(deployService.getDeployStatus(pathParam("applicationId").toInt()))
            }

            get("/{buildId}/log") {
                response(buildService.getBuildLog(pathParam("buildId").toInt()))
            }
        }
    }
}