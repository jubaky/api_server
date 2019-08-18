package org.jaram.jubaky.jenkins.repository

import com.cdancy.jenkins.rest.JenkinsClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.offbytwo.jenkins.JenkinsServer
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jaram.jubaky.createJenkinsApiStatusException
import org.jaram.jubaky.domain.checker.Build
import org.jaram.jubaky.domain.jenkins.*
import org.jaram.jubaky.enumuration.toBuildStatus
import org.jaram.jubaky.jenkins.JenkinsClientWithJson
import org.jaram.jubaky.jenkins.JenkinsClientWithText
import org.jaram.jubaky.jenkins.protocol.JobProtocol
import org.jaram.jubaky.jenkins.protocol.JobSpecProtocol
import org.jaram.jubaky.repository.JenkinsRepository
import org.jaram.jubaky.service.BuildCheckService
import org.w3c.dom.Element
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class JenkinsRepositoryImpl(
    private val jenkinsClientWithJson: JenkinsClientWithJson,
    private val jenkinsClientWithText: JenkinsClientWithText,
    private val buildCheckService: BuildCheckService,
    private val startDelayTime: Int
) : JenkinsRepository {

    private val jobConfigFilePath = javaClass.classLoader.getResource("job_config_default.xml")!!.file

    init {
        buildCheckService.jenkinsRepository = this
    }

    override suspend fun startPipeline(pipeline: Pipeline) {
        val server = JenkinsServer(URI("http://localhost:8080/jenkins"), "admin", "password")
        server.createJob("jobName", "configXML")

        val client = JenkinsClient.builder()
            .endPoint("http://127.0.0.1:8080") // Optional. Defaults to http://127.0.0.1:8080
            .credentials("admin:password") // Optional.
            .build()

        client.api().jobsApi().create(null, "jobName", "configXML")
    }

    /**
     * @TODO
     * Remove the dependency with Github
     */
    override suspend fun redirectGithubWebhook(headers: Map<String, String>, payload: String) {
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), payload)

        val request = jenkinsClientWithJson.redirectGithubWebhook(headers, requestBody)
        val response = request.await()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun getJob(jobName: String, branchName: String): Job {
        val branchedJobName = replaceNameWithBranch(jobName, branchName)
        val request = jenkinsClientWithJson.getJobInfo(branchedJobName)
        val response = request.await()

        if (response.isSuccessful) {
            val data = response.body()

            val healthReportList = data?.get("healthReport") as List<Map<*, *>>?
            var healthReportFirstMap = mapOf<Any, Any>() as Map<*, *>

            if (healthReportList?.isNotEmpty()!!) {
                healthReportFirstMap = healthReportList[0]
            }

            val lastBuildMap = data?.get("lastBuild") as Map<*, *>?
            val lastCompletedBuildMap = data?.get("lastCompletedBuild") as Map<*, *>?
            val lastFailedBuildMap = data?.get("lastFailedBuild") as Map<*, *>?
            val lastStableBuildMap = data?.get("lastStableBuild") as Map<*, *>?
            val lastSuccessfulBuildMap = data?.get("lastSuccessfulBuild") as Map<*, *>?
            val lastUnstableBuildMap = data?.get("lastUnstableBuild") as Map<*, *>?
            val lastUnsuccessfulBuildMap = data?.get("lastUnsuccessfulBuild") as Map<*, *>?

            val propertyList = data?.get("property") as List<Map<*, *>>?
            val parameterObject =
                propertyList?.find { m: Map<*, *> -> m["_class"] == "hudson.model.ParametersDefinitionProperty" }
            val parameterListOuter = parameterObject?.get("parameterDefinitions") as List<Map<*, *>>?

            val buildArgumentList = mutableListOf<BuildArgument>()

            parameterListOuter?.map {
                val parameter = BuildArgument(
                    name = (it["defaultParameterValue"] as Map<*, *>?)?.get("name") as String?,
                    defaultValue = (it["defaultParameterValue"] as Map<*, *>?)?.get("value") as String?,
                    description = it["description"] as String?
                )

                buildArgumentList.add(parameter)
            }

            val fullName = data?.get("fullName") as String?
            val displayName = data?.get("displayName") as String?
            val displayNameOrNull = data?.get("displayNameOrNull") as String?
            val description = data?.get("description") as String?
            val buildable = data?.get("buildable") as Boolean?
            val concurrentBuild = data?.get("concurrentBuild") as Boolean?

            val healthScore = healthReportFirstMap["score"] as Int?
            val healthDescription = healthReportFirstMap["description"] as String?

            val inQueue = data?.get("inQueue") as Boolean?
            val keepDependencies = data?.get("keepDependencies") as Boolean?

            val lastBuildNumber = lastBuildMap?.get("number") as Int?
            val lastCompletedBuildNumber = lastCompletedBuildMap?.get("number") as Int?
            val lastFailedBuildNumber = lastFailedBuildMap?.get("number") as Int?
            val lastStableBuildNumber = lastStableBuildMap?.get("number") as Int?
            val lastSuccessfulBuildNumber = lastSuccessfulBuildMap?.get("number") as Int?
            val lastUnstableBuildNumber = lastUnstableBuildMap?.get("number") as Int?
            val lastUnsuccessfulBuildNumber = lastUnsuccessfulBuildMap?.get("number") as Int?

            val jobProtocol = JobProtocol(
                name = jobName,
                fullName = fullName,
                displayName = displayName,
                displayNameOrNull = displayNameOrNull,
                description = description,
                buildable = buildable,
                concurrentBuild = concurrentBuild,
                healthScore = healthScore,
                healthDescription = healthDescription,
                inQueue = inQueue,
                keepDependencies = keepDependencies,
                lastBuildNumber = lastBuildNumber,
                lastCompletedBuildNumber = lastCompletedBuildNumber,
                lastFailedBuildNumber = lastFailedBuildNumber,
                lastStableBuildNumber = lastStableBuildNumber,
                lastSuccessfulBuildNumber = lastSuccessfulBuildNumber,
                lastUnstableBuildNumber = lastUnstableBuildNumber,
                lastUnsuccessfulBuildNumber = lastUnsuccessfulBuildNumber,
                buildArgumentList = buildArgumentList
            )

            return jobProtocol.toDomainModel()
        } else {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun getJobSpec(jobName: String, branchName: String, buildNumber: Int): JobSpec {
        val branchedJobName = replaceNameWithBranch(jobName, branchName)
        val request = jenkinsClientWithJson.getJobSpecInfo(branchedJobName, buildNumber)
        val response = request.await()

        if (response.isSuccessful) {
            val data = response.body()

            val buildArgumentList = mutableListOf<BuildArgument>()

            val actionsList = data?.get("actions") as List<Map<*, *>>?
            val parameterObject = actionsList?.find { m: Map<*, *> -> m["_class"] == "hudson.model.ParametersAction" }
            val parametersList = parameterObject?.get("parameters") as List<Map<*, *>>?

            parametersList?.map {
                val buildArgument = BuildArgument(
                    name = it["name"] as String?,
                    value = it["value"] as String?
                )

                buildArgumentList.add(buildArgument)
            }

            val timeDataObject = actionsList?.find { m: Map<*, *> -> m["_class"] == "jenkins.metrics.impl.TimeInQueueAction" }

            val fullDisplayName = data?.get("fullDisplayName") as String?
            val number = data?.get("number") as Int?
            val remoteUrlsList = data?.get("remoteUrls") as List<String>?
            val building = data?.get("building") as Boolean?
            val description = data?.get("description") as String?
            val buildDuration = timeDataObject?.get("buildingDurationMillis") as Int?
            val inQueueDuration = timeDataObject?.get("blockedDurationMillis") as Int?
            val estimatedDuration = data?.get("estimatedDuration") as Int?
            val queueId = data?.get("queueId") as Int?
            val keepLog = data?.get("keepLog") as Boolean?
            val result = data?.get("result") as String?
            val createTimestamp = data?.get("timestamp") as Long

            val jobSpecInfo = JobSpecProtocol(
                name = jobName,
                fullDisplayName = fullDisplayName,
                number = number,
                buildArgumentList = buildArgumentList,
                remoteUrlsList = remoteUrlsList,
                building = building,
                description = description,
                buildDuration = buildDuration,
                inQueueDuration = inQueueDuration,
                estimatedDuration = estimatedDuration,
                queueId = queueId,
                keepLog = keepLog,
                result = result,
                createTimestamp = createTimestamp
            )

            return jobSpecInfo.toDomainModel()
        } else {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun getJobLog(jobName: String, branchName: String, buildNumber: Int): JobLog {
        val branchedJobName = replaceNameWithBranch(jobName, branchName)
        val request = jenkinsClientWithText.getJobLog(branchedJobName, buildNumber)
        val response = request.await()

        if (response.isSuccessful) {
            return JobLog(
                jobName = jobName,
                log = response.body() ?: ""
            )
        } else {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun createJob(jobName: String, configData: JobConfig) {
        val branchedJobName = replaceNameWithBranch(jobName, configData.githubBranch)
        val file = File(jobConfigFilePath)
        val updatedFile = File("/tmp/${branchedJobName}_job_config_default.xml")

        if (updatedFile.exists()) {
            updatedFile.delete()
        }

        updatedFile.createNewFile()

        updateConfigXml(file, updatedFile, configData)
        val fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), updatedFile)

        val request = jenkinsClientWithJson.createJob(branchedJobName, fileRequestBody)
        val response = request.await()

        updatedFile.delete()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun deleteJob(jobName: String, branchName: String) {
        val branchedJobName = replaceNameWithBranch(jobName, branchName)
        val request = jenkinsClientWithJson.deleteJob(branchedJobName)
        val response = request.await()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun updateJob(jobName: String, configData: JobConfig) {
        val branchedJobName = replaceNameWithBranch(jobName, configData.githubBranch)
        val file = File(jobConfigFilePath)
        val updatedFile = File("/tmp/${branchedJobName}_job_config_default.xml")

        if (updatedFile.exists()) {
            updatedFile.delete()
        }

        updatedFile.createNewFile()

        updateConfigXml(file, updatedFile, configData)
        val fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), updatedFile)

        val request = jenkinsClientWithJson.updateJob(branchedJobName, fileRequestBody)
        val response = request.await()

        updatedFile.delete()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun buildWithParameters(jobName: String, branchName: String, buildArgumentList: List<BuildArgument>) {
        val branchedJobName = replaceNameWithBranch(jobName, branchName)
        val buildArgumentMap = mutableMapOf<String, String>()
        buildArgumentList.map {
            buildArgumentMap[it.name ?: ""] = it.defaultValue ?: ""
        }

        // Check if build is duplicated
        buildCheckService.checkBuildDuplication(jobName, branchName)

        val request = jenkinsClientWithJson.buildWithParameters(
            branchedJobName,
            buildArgumentMap
        )
        val response = request.await()

        if (response.isSuccessful) {
            TimeUnit.MILLISECONDS.sleep(startDelayTime.toLong())

            val currentBuildNumber = getJob(jobName, branchName).lastBuildNumber + 1
            val build = Build(
                name = jobName,
                branch = branchName,
                buildNumber = currentBuildNumber,
                status = toBuildStatus("PENDING")
            )

            buildCheckService.getPendingBuildList().add(build)
        } else {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun getPendingBuildList(): List<String> {
        val request = jenkinsClientWithJson.getPenddingBuildList()
        val response = request.await()

        if (response.isSuccessful) {
            val pendingBuildJobNameList = mutableListOf<String>()
            val data = response.body()

            val buildList = data?.get("items") as List<Map<String, Any>>

            if (buildList.isNotEmpty()) {
                buildList.map { build ->
                    val task = build["task"] as Map<String, String>

                    pendingBuildJobNameList.add(task["name"] ?: "")
                }
            }

            return pendingBuildJobNameList
        } else {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun createCredentials(credentials: Credentials) {
        val mapper = ObjectMapper()
        val rootNode = mapper.createObjectNode()
        rootNode.put("\"\"", "0")

        val credentialsNode = mapper.createObjectNode()
        credentialsNode.put("scope", credentials.scope)
        credentialsNode.put("id", credentials.key)
        credentialsNode.put("username", credentials.username)
        credentialsNode.put("password", credentials.password)
        credentialsNode.put("description", credentials.description)
        credentialsNode.put("\$class", "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")

        rootNode.set("credentials", credentialsNode)

        val credData = mapper.writeValueAsString(rootNode)

        val request = jenkinsClientWithJson.createCredentials(credData)
        val response = request.await()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    override suspend fun deleteCredentials(key: String) {
        val request = jenkinsClientWithJson.deleteCredentials(key)
        val response = request.await()

        if (!response.isSuccessful) {
            throw createJenkinsApiStatusException(response.code())
        }
    }

    private fun updateConfigXml(xmlFile: File, updatedXmlFile: File, configXmlData: JobConfig) {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)

        doc.documentElement.normalize()
        val root = doc.documentElement

        // @Description
        val description = root.getElementsByTagName("description").item(0)
        description.textContent = configXmlData.description

        // @KeepDependencies
        val keepDependencies = root.getElementsByTagName("keepDependencies").item(0)
        keepDependencies.textContent = configXmlData.keepDependencies

        // @Parameters
        val parametersDefinitionElement =
            root.getElementsByTagName("hudson.model.ParametersDefinitionProperty").item(0) as Element

        val parameterDefinitionsOld = parametersDefinitionElement.getElementsByTagName("parameterDefinitions").item(0)
        parametersDefinitionElement.removeChild(parameterDefinitionsOld)

        val parameterDefinitionsNew = doc.createElement("parameterDefinitions")
        parametersDefinitionElement.appendChild(parameterDefinitionsNew)

        for (parameter in configXmlData.buildArgumentList) {
            val parameterDefinition = doc.createElement("hudson.model.StringParameterDefinition")

            val parameterName = doc.createElement("name")
            val parameterDefaultValue = doc.createElement("defaultValue")
            val parameterDescription = doc.createElement("description")
            val parameterTrim = doc.createElement("trim")

            parameterName.appendChild(doc.createTextNode(parameter.name))
            parameterDefaultValue.appendChild(doc.createTextNode(parameter.defaultValue))
            parameterDescription.appendChild(doc.createTextNode(parameter.description))
            parameterTrim.appendChild(doc.createTextNode(parameter.trim))

            parameterDefinition.appendChild(parameterName)
            parameterDefinition.appendChild(parameterDescription)
            parameterDefinition.appendChild(parameterDefaultValue)
            parameterDefinition.appendChild(parameterTrim)

            parameterDefinitionsNew.appendChild(parameterDefinition)
        }

        // @Github
        val gitHubElement = root.getElementsByTagName("hudson.plugins.git.UserRemoteConfig").item(0) as Element
        val githubUrl = gitHubElement.getElementsByTagName("url").item(0)
        val githubCredentials = gitHubElement.getElementsByTagName("credentialsId").item(0)

        githubUrl.textContent = configXmlData.githubUrl
        githubCredentials.textContent = configXmlData.githubCredentials

        val githubBranchElement = root.getElementsByTagName("hudson.plugins.git.BranchSpec").item(0) as Element
        val githubBranch = githubBranchElement.getElementsByTagName("name").item(0)

        githubBranch.textContent = configXmlData.githubBranch

        // @Shell command
        val shellCommandElement = root.getElementsByTagName("hudson.tasks.Shell").item(0) as Element
        val shellCommand = shellCommandElement.getElementsByTagName("command").item(0)

        val builtShellCommand = buildShellCommand(
            configXmlData.githubBranch,
            configXmlData.beforeCommand,
            configXmlData.afterCommand,
            configXmlData.dockerArgument,
            configXmlData.buildArgumentList
        )

        shellCommand.textContent = builtShellCommand

        // Update the xml file
        val transformer = TransformerFactory.newInstance().newTransformer()
        val domSource = DOMSource(doc)

        val streamResult = StreamResult(updatedXmlFile)
        transformer.transform(domSource, streamResult)
    }

    override fun replaceNameWithBranch(prefix: String, branchName: String): String {
        return "${prefix}_$branchName".replace("/", "_")
    }

    private fun buildShellCommand(
        branchName: String,
        beforeCommand: String,
        afterCommand: String,
        dockerArgument: DockerArgument,
        buildArgumentList: List<BuildArgument>?
    ): String {
        /**
         * @TODO
         * dev, test, prod 환경에 맞춰서 tag 핸들링하기
         */
        val branchedJobName = replaceNameWithBranch("branch", branchName)
        val dockerImageValue =
            "${dockerArgument.imageUsername}/${dockerArgument.imageName}:${branchedJobName}_${dockerArgument.imageVersion}"
        var buildParameterValue = ""

        if (buildArgumentList != null) {
            for (buildParameter in buildArgumentList) {
                buildParameterValue += "--build-arg ${buildParameter.name}=${buildParameter.defaultValue} "
            }
        }

        return "" +
                "$beforeCommand\n" +
                "docker login -u=${dockerArgument.dockerUsername} -p=${dockerArgument.dockerPassword} > /dev/null 2>&1 &\n" +
                "docker build -t $dockerImageValue $buildParameterValue --force-rm=true .\n" +
                "docker push $dockerImageValue\n" +
                "docker rmi $dockerImageValue\n" +
                "$afterCommand\n"
    }
}
