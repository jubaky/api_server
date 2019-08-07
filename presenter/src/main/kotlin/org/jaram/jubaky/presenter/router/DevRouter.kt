package org.jaram.jubaky.presenter.router

import io.ktor.application.call
import io.ktor.request.receiveText
import io.ktor.routing.*
import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.DockerArgument
import org.jaram.jubaky.domain.jenkins.JobConfig
import org.jaram.jubaky.presenter.ext.*
import org.jaram.jubaky.service.JenkinsService


fun Route.dev(jenkinsService: JenkinsService) {
    get("/") {
        response(
            "Jenkins Service"
        )
    }

    post("/github-webhook") {
        val headers = call.request.headers
        val payload = call.receiveText()

        val headersMap = mutableMapOf<String, String>()

        headersMap["Content-Type"] = headers["Content-Type"]!!
        headersMap["Expect"] = headers["Expect"]?: ""
        headersMap["User-Agent"] = headers["User-Agent"]!!
        headersMap["X-GitHub-Delivery"] = headers["X-GitHub-Delivery"]!!
        headersMap["X-GitHub-Event"] = headers["X-GitHub-Event"]!!

        response(
            jenkinsService.redirectGithubWebhook(headersMap, payload)
        )
    }

    route("/job") {

        /**
         * @path
         * - jobName : String
         */
        get("/{jobName}") {
            val jobName = pathParam("jobName")

            response(
                jenkinsService.getJob(jobName)
            )
        }

        /**
         * @path
         * - jobName : String
         * - buildNumber : String
         */
        get("/spec/{jobName}/{buildNumber}") {
            val jobName = pathParam("jobName")
            val buildNumber = pathParam("buildNumber")

            response(
                jenkinsService.getJobSpec(jobName, buildNumber)
            )
        }

        /**
         * @path
         * - jobName : String
         * - buildNumber : String
         */
        get("/log/{jobName}/{buildNumber}") {
            val jobName = pathParam("jobName")
            val buildNumber = pathParam("buildNumber")

            response(
                jenkinsService.getJobLog(jobName, buildNumber)
            )
        }

        /**
         * @param
         * - jobName : String
         *
         * < Config Parameters >
         * - description : String
         * - keepDependencies : Boolean?: false
         * - githubUrl : String
         * - githubCredentials : String
         * - githubBranch : String
         * - beforeCommand : String
         * - afterCommand : String
         *
         * < Docker Image Arguments >
         * - dockerUsername : String
         * - dockerPassword : String
         * - imageUsername : String
         * - imageName : String
         * - imageVersion : String
         *
         * < Docker Build Arguments >
         * - dockerArgumentName : List<String>
         * - dockerArgumentDefaultValue: List<String>
         * - dockerArgumentDescription: List<String>
         */
        post("/") {
            val jobName = bodyParam("jobName")

            // Docker image arguments
            val dockerArgument = DockerArgument(
                dockerUsername = bodyParam("dockerUsername"),
                dockerPassword = bodyParam("dockerPassword"),
                imageUsername = bodyParam("imageUsername"),
                imageName = bodyParam("imageName"),
                imageVersion = bodyParam("imageVersion")
            )

            // Docker build arguments
            val buildArgNameList = bodyParamListSafe("dockerArgumentName")
            val buildArgDefaultValueList = bodyParamListSafe("dockerArgumentDefaultValue")
            val buildArgDescriptionList = bodyParamListSafe("dockerArgumentDescription")

            val buildArgumentList = mutableListOf<BuildArgument>()

            if (buildArgNameList != null && buildArgDefaultValueList != null && buildArgDescriptionList != null) {
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
                jobName = jobName,
                description = bodyParam("description"),
                keepDependencies = bodyParam("keepDependencies"),
                buildArgumentList = buildArgumentList,
                githubUrl = bodyParam("githubUrl"),
                githubCredentials = bodyParam("githubCredentials"),
                githubBranch = bodyParam("githubBranch"),
                beforeCommand = bodyParam("beforeCommand"),
                afterCommand = bodyParam("afterCommand"),
                dockerArgument = dockerArgument
            )

            response(
                jenkinsService.createJob(jobName, configData)
            )
        }

        /**
         * @path
         * - jobName : String
         */
        delete("/{jobName}") {
            val jobName = pathParam("jobName")

            response(
                jenkinsService.deleteJob(jobName)
            )
        }

        /**
         * @param
         * - jobName : String
         *
         * < config.xml Parameters >
         * - description : String
         * - keepDependencies : Boolean
         * - githubUrl : String
         * - githubCredentials : String
         * - githubBranch : String
         * - beforeCommand : String
         * - afterCommand : String
         *
         * < Docker Image Arguments >
         * - dockerUsername : String
         * - dockerPassword : String
         * - imageUsername : String
         * - imageName : String
         * - imageVersion : String
         *
         * < Docker Build Arguments >
         * - dockerArgumentName : List<String>
         * - dockerArgumentDefaultValue: List<String>
         * - dockerArgumentDescription: List<String>
         */
        put("/") {
            val jobName = bodyParam("jobName")

            // Docker image arguments
            val dockerArgument = DockerArgument(
                dockerUsername = bodyParam("dockerUsername"),
                dockerPassword = bodyParam("dockerPassword"),
                imageUsername = bodyParam("imageUsername"),
                imageName = bodyParam("imageName"),
                imageVersion = bodyParam("imageVersion")
            )

            // Docker build arguments
            val buildArgNameList = bodyParamListSafe("dockerArgumentName")
            val buildArgDefaultValueList = bodyParamListSafe("dockerArgumentDefaultValue")
            val buildArgDescriptionList = bodyParamListSafe("dockerArgumentDescription")

            val buildArgumentList = mutableListOf<BuildArgument>()

            if (buildArgNameList != null && buildArgDefaultValueList != null && buildArgDescriptionList != null) {
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
                jobName = jobName,
                description = bodyParam("description"),
                keepDependencies = bodyParam("keepDependencies"),
                buildArgumentList = buildArgumentList,
                githubUrl = bodyParam("githubUrl"),
                githubCredentials = bodyParam("githubCredentials"),
                githubBranch = bodyParam("githubBranch"),
                beforeCommand = bodyParam("beforeCommand"),
                afterCommand = bodyParam("afterCommand"),
                dockerArgument = dockerArgument
            )

            response(
                jenkinsService.updateJob(jobName, configData)
            )
        }

        /**
         * @param
         * - jobName : String
         *
         * < Docker Build Arguments >
         * - dockerArgumentName : List<String>
         * - dockerArgumentDefaultValue: List<String>
         * - dockerArgumentDescription: List<String>
         */
        post("/buildWithParameters") {
            val jobName = bodyParam("jobName")

            // Docker build arguments
            val buildArgNameList = bodyParamListSafe("dockerArgumentName")
            val buildArgDefaultValueList = bodyParamListSafe("dockerArgumentDefaultValue")
            val buildArgDescriptionList = bodyParamListSafe("dockerArgumentDescription")

            val buildArgumentList = mutableListOf<BuildArgument>()

            if (buildArgNameList != null && buildArgDefaultValueList != null && buildArgDescriptionList != null) {
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
                jenkinsService.buildWithParameters(jobName, buildArgumentList)
            )
        }
    }

    route("/credentials") {

        /**
         * @param
         * - username : String
         * - password : String
         * - key : String
         * - description : String
         */
        post("/") {
            val username = bodyParam("username")
            val password = bodyParam("password")
            val key = bodyParam("key")
            val description = bodyParam("description")

            response(
                jenkinsService.createCredentials(username, password, key, description)
            )
        }

        /**
         * @query
         * - key : String
         */
        delete("/{key}") {
            val key = pathParam("key")

            response(
                jenkinsService.deleteCredentials(key)
            )
        }

        put("/") {

        }
    }
}