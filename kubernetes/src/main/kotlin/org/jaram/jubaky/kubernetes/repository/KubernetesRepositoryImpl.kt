package org.jaram.jubaky.kubernetes.repository

import io.kubernetes.client.models.*
import io.kubernetes.client.util.Yaml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jaram.jubaky.KubernetesObjectNotFoundException
import org.jaram.jubaky.domain.checker.toDeploy
import org.jaram.jubaky.domain.kubernetes.*
import org.jaram.jubaky.enumuration.DeployStatus
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.deployStatusToString
import org.jaram.jubaky.kubernetes.KubernetesApi
import org.jaram.jubaky.kubernetes.toDomainModel
import org.jaram.jubaky.protocol.DeployInfo
import org.jaram.jubaky.repository.*
import org.jaram.jubaky.service.DeployCheckService
import org.joda.time.DateTime

class KubernetesRepositoryImpl(
    private val applicationRepository: ApplicationRepository,
    private val buildRepository: BuildRepository,
    private val deployRepository: DeployRepository,
    private val templateRepository: TemplateRepository,
    private val deployCheckService: DeployCheckService,
    private val api: KubernetesApi
) : KubernetesRepository {

    init {
        deployCheckService.kubernetesRepository = this
    }

    override suspend fun getDaemonSet(daemonSetName: String, namespace: String): DaemonSet = withContext(Dispatchers.IO) {
        api.getDaemonSet(daemonSetName, namespace).toDomainModel()
    }

    override suspend fun getDaemonSetList(namespace: String?): List<DaemonSet> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getDaemonSetList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getDaemonSetListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createDaemonSet(yaml: String, namespace: String): DaemonSet = withContext(Dispatchers.IO) {
        api.createDaemonSet(namespace, Yaml.load(yaml) as V1beta1DaemonSet).toDomainModel()
    }

    override suspend fun replaceDaemonSet(name: String, yaml: String, namespace: String): DaemonSet = withContext(Dispatchers.IO) {
        api.replaceDaemonSet(name, namespace, Yaml.load(yaml) as V1beta1DaemonSet).toDomainModel()
    }

    override suspend fun deleteDaemonSet(daemonSetName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteDaemonSet(daemonSetName, namespace)
    }

    override suspend fun getDeployment(deploymentName: String, namespace: String): Deployment = withContext(Dispatchers.IO) {
        api.getDeployment(deploymentName, namespace).toDomainModel()
    }

    override suspend fun getDeploymentList(namespace: String?): List<Deployment> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getDeploymentList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getDeploymentListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createDeployment(buildId: Int, yaml: String, namespace: String): Deployment = withContext(Dispatchers.IO) {
        val buildInfo = buildRepository.getBuildInfo(buildId)
        val deployment = api.createDeployment(namespace, Yaml.load(yaml) as ExtensionsV1beta1Deployment).toDomainModel()

        val applicationInfo = applicationRepository.getApplicationInfo(buildInfo.applicationName)

        templateRepository.createTemplate(
            name = "",  // Don't know what name is in
            kind = deployment.kind ?: "UNKNOWN",
            content = yaml,
            applicationId = applicationInfo.id
        )

        deployRepository.createDeploy(
            buildId = buildId,
            namespace = namespace,
            status = deployStatusToString(DeployStatus.SUCCESS),
            applicationId = applicationInfo.id,
            templateId = templateRepository.getTemplateInfo(applicationInfo.name).id,
            creatorId = applicationRepository.getUserId(applicationInfo.id)
        )

        deployment
    }

    override suspend fun replaceDeployment(deployInfo: DeployInfo): Deployment = withContext(Dispatchers.IO) {
        val template = templateRepository.getTemplateInfo(deployInfo.applicationName)

        println(deployInfo)
        println(template)
        println(template.yaml)

        val deployment = api.replaceDeployment(
            deployInfo.applicationName,
            deployInfo.namespace,
            Yaml.load(template.yaml) as ExtensionsV1beta1Deployment
        ).toDomainModel()

        val deploy = toDeploy(deployInfo.id, deployment, DeployStatus.PROGRESS)

        if (!deployCheckService.checkDeployDuplication(deploy))
            deployCheckService.getProgressDeployList().add(deploy)

        deployment
    }

    override suspend fun deleteDeployment(deploymentName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteDeployment(deploymentName, namespace)
    }

    override suspend fun getNamespace(namespaceName: String): Namespace = withContext(Dispatchers.IO) {
        api.getNamespace(namespaceName).toDomainModel()
    }

    override suspend fun getNamespaceList(): List<Namespace> = withContext(Dispatchers.IO) {
        api.getNamespaceList().items.map { it.toDomainModel() }
    }

    override suspend fun createNamespace(yaml: String): Namespace = withContext(Dispatchers.IO) {
        api.createNamespace(Yaml.load(yaml) as V1Namespace).toDomainModel()
    }

    override suspend fun replaceNamespace(name: String, yaml: String): Namespace = withContext(Dispatchers.IO) {
        api.replaceNamespace(name, Yaml.load(yaml) as V1Namespace).toDomainModel()
    }

    override suspend fun deleteNamespace(namespaceName: String) = withContext(Dispatchers.IO) {
        api.deleteNamespace(namespaceName)
    }

    override suspend fun getNode(nodeName: String): Node = withContext(Dispatchers.IO) {
        api.getNode(nodeName).toDomainModel()
    }

    override suspend fun getNodeList(): List<Node> = withContext(Dispatchers.IO) {
        api.getNodeList().items.map { it.toDomainModel() }
    }

    override suspend fun deleteNode(nodeName: String) = withContext(Dispatchers.IO) {
        api.deleteNode(nodeName)
    }

    override suspend fun getPod(podName: String, namespace: String): Pod = withContext(Dispatchers.IO) {
        api.getPod(podName, namespace).toDomainModel()
    }

    override suspend fun getPodList(namespace: String?): List<Pod> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getPodList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getPodListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun getPodLog(podName: String, namespace: String): String = withContext(Dispatchers.IO) {
        api.getPodLog(podName, namespace)
    }

    override suspend fun createPod(yaml: String, namespace: String): Pod = withContext(Dispatchers.IO) {
        api.createPod(namespace, Yaml.load(yaml) as V1Pod).toDomainModel()
    }

    override suspend fun replacePod(name: String, yaml: String, namespace: String): Pod = withContext(Dispatchers.IO) {
        api.replacePod(name, namespace, Yaml.load(yaml) as V1Pod).toDomainModel()
    }

    override suspend fun deletePod(podName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deletePod(podName, namespace)
    }

    override suspend fun getReplicaSet(replicaSetName: String, namespace: String): ReplicaSet = withContext(Dispatchers.IO) {
        api.getReplicaSet(replicaSetName, namespace).toDomainModel()
    }

    override suspend fun getReplicaSetList(namespace: String?): List<ReplicaSet> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getReplicaSetList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getReplicaSetListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createReplicaSet(yaml: String, namespace: String): ReplicaSet = withContext(Dispatchers.IO) {
        api.createReplicaSet(namespace, Yaml.load(yaml) as V1ReplicaSet).toDomainModel()
    }

    override suspend fun replaceReplicaSet(name: String, yaml: String, namespace: String): ReplicaSet = withContext(Dispatchers.IO) {
        api.replaceReplicaSet(name, namespace, Yaml.load(yaml) as V1ReplicaSet).toDomainModel()
    }

    override suspend fun deleteReplicaSet(replicaSetName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteReplicaSet(replicaSetName, namespace)
    }

    override suspend fun getSecret(secretName: String, namespace: String): Secret = withContext(Dispatchers.IO) {
        api.getSecret(secretName, namespace).toDomainModel()
    }

    override suspend fun getSecretList(namespace: String?): List<Secret> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getSecretList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getSecretListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createSecret(yaml: String, namespace: String): Secret = withContext(Dispatchers.IO) {
        api.createSecret(namespace, Yaml.load(yaml) as V1Secret).toDomainModel()
    }

    override suspend fun replaceSecret(name: String, yaml: String, namespace: String): Secret = withContext(Dispatchers.IO) {
        api.replaceSecret(name, namespace, Yaml.load(yaml) as V1Secret).toDomainModel()
    }

    override suspend fun deleteSecret(secretName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteSecret(secretName, namespace)
    }

    override suspend fun getService(serviceName: String, namespace: String): Service = withContext(Dispatchers.IO) {
        api.getService(serviceName, namespace).toDomainModel()
    }

    override suspend fun getServiceList(namespace: String?): List<Service> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getServiceList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getServiceListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createService(yaml: String, namespace: String): Service = withContext(Dispatchers.IO) {
        api.createService(namespace, Yaml.load(yaml) as V1Service).toDomainModel()
    }

    override suspend fun replaceService(name: String, yaml: String, namespace: String): Service = withContext(Dispatchers.IO) {
        api.replaceService(name, namespace, Yaml.load(yaml) as V1Service).toDomainModel()
    }

    override suspend fun deleteService(serviceName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteService(serviceName, namespace)
    }

    override suspend fun getStatefulSet(statefulSetName: String, namespace: String): StatefulSet = withContext(Dispatchers.IO) {
        api.getStatefulSet(statefulSetName, namespace).toDomainModel()
    }

    override suspend fun getStatefulSetList(namespace: String?): List<StatefulSet> = withContext(Dispatchers.IO) {
        if (namespace != null) {
            api.getStatefulSetList(namespace).items.map { it.toDomainModel() }
        } else {
            api.getStatefulSetListForAllNamespaces().items.map { it.toDomainModel() }
        }
    }

    override suspend fun createStatefulSet(yaml: String, namespace: String): StatefulSet = withContext(Dispatchers.IO) {
        api.createStatefulSet(namespace, Yaml.load(yaml) as V1StatefulSet).toDomainModel()
    }

    override suspend fun replaceStatefulSet(name: String, yaml: String, namespace: String): StatefulSet = withContext(Dispatchers.IO) {
        api.replaceStatefulSet(name, namespace, Yaml.load(yaml) as V1StatefulSet).toDomainModel()
    }

    override suspend fun deleteStatefulSet(statefulSetName: String, namespace: String) = withContext(Dispatchers.IO) {
        api.deleteStatefulSet(statefulSetName, namespace)
    }

    override suspend fun createObject(buildId: Int, yaml: String, namespace: String) {
        when(Yaml.load(yaml)) {
//            is V1beta1DaemonSet -> createDaemonSet()
            is ExtensionsV1beta1Deployment -> createDeployment(buildId, yaml, namespace)
//            is V1Namespace -> createNamespace()
//            is V1Pod -> createPod()
//            is V1ReplicaSet -> createReplicaSet()
//            is V1Secret -> createSecret()
//            is V1Service -> createService()
//            is V1StatefulSet -> createStatefulSet()
            else -> throw KubernetesObjectNotFoundException()
        }
    }
}