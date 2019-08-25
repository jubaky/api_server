package org.jaram.jubaky.presenter.router

import io.ktor.application.call
import io.ktor.request.receiveText
import io.ktor.routing.*
import org.jaram.jubaky.domain.jenkins.BuildArgument
import org.jaram.jubaky.domain.jenkins.Credentials
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
        get("/{jobName}/{branchName}") {
            val jobName = pathParam("jobName")
            val branchName = pathParam("branchName")

//            response(
//                jenkinsService.getJob(jobName, branchName)
//            )
        }

        /**
         * @path
         * - jobName : String
         * - branchName : String
         * - buildNumber : String
         */
        get("/spec/{jobName}/{branchName}/{buildNumber}") {
            val jobName = pathParam("jobName")
            val branchName = pathParam("branchName")
            val buildNumber = pathParam("buildNumber").toInt()

//            response(
//                jenkinsService.getJobSpec(jobName, branchName, buildNumber)
//            )
        }

        /**
         * @path
         * - jobName : String
         * - branchName : String
         * - buildNumber : String
         */
        get("/log/{jobName}/{branchName}/{buildNumber}") {
            val jobName = pathParam("jobName")
            val branchName = pathParam("branchName")
            val buildNumber = pathParam("buildNumber").toInt()

//            response(
//                jenkinsService.getJobLog(jobName, branchName, buildNumber).log
//            )
        }

        /**
         * @param
         * - job_name : String
         *
         * < Config Parameters >
         * - description : String (optional)
         * - keep_dependencies : Boolean (optional)
         * - github_url : String
         * - github_credentials : String
         * - github_branch : String
         * - before_command : String (optional)
         * - after_command : String (optional)
         *
         * < Docker Image Arguments >
         * - docker_username : String
         * - docker_password : String
         * - image_username : String
         * - image_name : String
         * - image_version : String
         *
         * < Docker Build Arguments >
         * - docker_argument_name : List<String> (optional)
         * - docker_argument_default_value: List<String> (optional)
         * - docker_argument_description: List<String> (optional)
         */
        post("/") {
            val jobName = bodyParam("job_name")

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
//                jobName = jobName,
                description = bodyParam("description", "Job description"),
                keepDependencies = bodyParam("keep_dependencies", "false"),
                buildArgumentList = buildArgumentList,
                githubUrl = bodyParam("github_url"),
                githubCredentials = bodyParam("github_credentials"),
                githubBranch = bodyParam("github_branch"),
                beforeCommand = bodyParam("before_command", ""),
                afterCommand = bodyParam("after_command", ""),
                dockerArgument = dockerArgument
            )

//            response(
//                jenkinsService.createJob(jobName, configData)
//            )
        }

        /**
         * @path
         * - jobName : String
         */
        delete("/{jobName}/{branchName}") {
            val jobName = pathParam("jobName")
            val branchName = pathParam("branchName")

            response(
                jenkinsService.deleteJob(jobName, branchName)
            )
        }

        /**
         * @param
         * - job_name : String
         *
         * < config.xml Parameters >
         * - description : String (optional)
         * - keep_dependencies : Boolean (optional)
         * - github_url : String
         * - github_credentials : String
         * - github_branch : String
         * - before_command : String (optional)
         * - after_command : String (optional)
         *
         * < Docker Image Arguments >
         * - docker_username : String
         * - docker_password : String
         * - image_username : String
         * - image_name : String
         * - image_version : String
         *
         * < Docker Build Arguments >
         * - docker_argument_name : List<String> (optional)
         * - docker_argument_default_value: List<String> (optional)
         * - docker_argument_description: List<String> (optional)
         */
        put("/") {
            val jobName = bodyParam("job_name")

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
//                jobName = jobName,
                description = bodyParam("description", "Job description"),
                keepDependencies = bodyParam("keep_dependencies", "false"),
                buildArgumentList = buildArgumentList,
                githubUrl = bodyParam("github_url"),
                githubCredentials = bodyParam("github_credentials"),
                githubBranch = bodyParam("github_branch"),
                beforeCommand = bodyParam("before_command", ""),
                afterCommand = bodyParam("after_command", ""),
                dockerArgument = dockerArgument
            )

            response(
                jenkinsService.updateJob(jobName, configData)
            )
        }

        /**
         * @param
         * - job_name : String
         * - branch_name : String
         *
         * < Docker Build Arguments >
         * - docker_argument_name : List<String> (optional)
         * - docker_argument_default_value: List<String> (optional)
         * - docker_argument_description: List<String> (optional)
         */
        post("/buildWithParameters") {
            val jobName = bodyParam("job_name")
            val branchName = bodyParam("branch_param")

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

//            response(
//                jenkinsService.buildWithParameters(jobName, branchName, buildArgumentList)
//            )
        }
    }

    get("/pendingBuildList") {
        response(
            jenkinsService.getPendingBuildList()
        )
    }

    route("/credentials") {

        /**
         * @param
         * - username : String
         * - password : String
         * - key : String
         * - description : String (optional)
         * - scope : String (optional)
         */
        post("/") {
            val credentials = Credentials(
                username = bodyParam("username"),
                password = bodyParam("password"),
                key = bodyParam("key"),
                description = bodyParam("description", "Credentials description"),
                scope = bodyParam("scope", "GLOBAL")
            )

            response(
                jenkinsService.createCredentials(credentials)
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

/**
 * @query
 * - key : String
 */