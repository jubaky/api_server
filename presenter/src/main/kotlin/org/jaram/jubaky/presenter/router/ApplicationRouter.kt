package org.jaram.jubaky.presenter.router

import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.presenter.ext.*
import org.jaram.jubaky.service.ApplicationService
import org.jaram.jubaky.service.BuildService
import org.jaram.jubaky.service.DeployService

fun Route.app(
    applicationService: ApplicationService,
    buildService: BuildService,
    deployService: DeployService
) {
    get {
        response(applicationService.getApplicationList())
    }

    route("/{applicationId}") {
        get {
            response(applicationService.getApplicationInfo(pathParam("applicationId").toInt()))
        }

        route("/build") {
            get {
                val applicationId = pathParam("applicationId").toInt()
                val topSize = queryParamSafe("top")?.toIntOrNull() ?: 10

                response(buildService.getRecentBuildList(applicationId, topSize))
            }

            get("/{buildId}") {
                response(buildService.getBuildInfo(pathParam("buildId").toInt()))
            }
        }

        route("/deploy") {
            get {
                val topSize = queryParamSafe("top")?.toIntOrNull() ?: 10
                val namespace = queryParamSafe("namespace")

                response(deployService.getRecentDeployList(topSize, namespace))
            }

            get("/{deployId}") {
                response(deployService.getDeployInfo(pathParam("deployId").toInt()))
            }

            post {
                val buildId = bodyParam("build_id").toInt()
                val namespace = bodyParam("namespace")

                response(deployService.runDeploy(buildId, namespace))
            }
        }

        route("/branch") {
            get {
                response(applicationService.getBranchList(pathParam("applicationId").toInt()))
            }

            route("/{branchName}") {
                get("/build") {
                    response(
                        buildService.getRecentBuildList(
                            pathParam("applicationId").toInt(),
                            queryParamSafe("top")?.toIntOrNull() ?: 10,
                            pathParam("branchName")
                        )
                    )
                }

                post("/build") {
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

                    response(
                        buildService.runBuild(
                            pathParam("applicationId").toInt(),
                            pathParam("branchName"),
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
}