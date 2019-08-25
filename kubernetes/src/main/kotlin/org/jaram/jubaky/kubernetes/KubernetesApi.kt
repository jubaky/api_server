package org.jaram.jubaky.kubernetes

import io.kubernetes.client.ApiClient
import io.kubernetes.client.ProgressRequestBody
import io.kubernetes.client.ProgressResponseBody
import io.kubernetes.client.apis.AppsV1Api
import io.kubernetes.client.apis.CoreV1Api
import io.kubernetes.client.apis.ExtensionsV1beta1Api
import io.kubernetes.client.models.*
import io.kubernetes.client.util.Config
import org.jaram.jubaky.createKubernetesApiException
import java.io.File

open class KubernetesApi(configFile: File)  {

    private val client: ApiClient
    private val coreV1Api: CoreV1Api
    private val appsV1Api: AppsV1Api
    private val extV1beta1Api: ExtensionsV1beta1Api

    init {
        val inputStream = configFile.inputStream()

        client = Config.fromConfig(inputStream)

        inputStream.close()

        coreV1Api = CoreV1Api(client)
        appsV1Api = AppsV1Api(client)
        extV1beta1Api = ExtensionsV1beta1Api(client)
    }

    fun getDaemonSet(
        daemonSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1beta1DaemonSet {
        try {
            return extV1beta1Api.readNamespacedDaemonSet(daemonSetName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getDaemonSetList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1beta1DaemonSetList {
        try {
            return extV1beta1Api.listNamespacedDaemonSet(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getDaemonSetListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1beta1DaemonSetList {
        try {
            return extV1beta1Api.listDaemonSetForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createDaemonSet(
        namespace: String? = "default",
        daemonSet: V1beta1DaemonSet,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1beta1DaemonSet {
        try {
            return extV1beta1Api.createNamespacedDaemonSet(namespace, daemonSet, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceDaemonSet(
        daemonSetName: String,
        namespace: String? = "default",
        daemonSet: V1beta1DaemonSet,
        pretty: String? = null,
        dryRun: String? = null
    ): V1beta1DaemonSet {
        try {
            return extV1beta1Api.replaceNamespacedDaemonSet(daemonSetName, namespace, daemonSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchDaemonSet(
        daemonSetName: String,
        namespace: String? = "default",
        daemonSet: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1beta1DaemonSet {
        try {
            return extV1beta1Api.patchNamespacedDaemonSet(daemonSetName, namespace, daemonSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteDaemonSet(
        daemonSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = extV1beta1Api.deleteNamespacedDaemonSetCall(daemonSetName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getDeployment(
        deploymentName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): ExtensionsV1beta1Deployment {
        try {
            return extV1beta1Api.readNamespacedDeployment(deploymentName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getDeploymentList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): ExtensionsV1beta1DeploymentList {
        try {
            return extV1beta1Api.listNamespacedDeployment(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getDeploymentListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): ExtensionsV1beta1DeploymentList {
        try {
            return extV1beta1Api.listDeploymentForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createDeployment(
        namespace: String? = "default",
        deployment: ExtensionsV1beta1Deployment,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): ExtensionsV1beta1Deployment {
        try {
            return extV1beta1Api.createNamespacedDeployment(namespace, deployment, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceDeployment(
        deploymentName: String,
        namespace: String? = "default",
        deployment: ExtensionsV1beta1Deployment,
        pretty: String? = null,
        dryRun: String? = null
    ): ExtensionsV1beta1Deployment {
        try {
            return extV1beta1Api.replaceNamespacedDeployment(deploymentName, namespace, deployment, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchDeployment(
        deploymentName: String,
        namespace: String? = "default",
        deployment: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): ExtensionsV1beta1Deployment {
        try {
            return extV1beta1Api.patchNamespacedDeployment(deploymentName, namespace, deployment, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteDeployment(
        deploymentName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = extV1beta1Api.deleteNamespacedDeploymentCall(deploymentName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getNamespace(
        namespaceName: String,
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1Namespace {
        try {
            return coreV1Api.readNamespace(namespaceName, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getNamespaceList(
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1NamespaceList {
        try {
            return coreV1Api.listNamespace(includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createNamespace(
        namespace: V1Namespace,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Namespace {
        try {
            return coreV1Api.createNamespace(namespace, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceNamespace(
        namespaceName: String,
        namespace: V1Namespace,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Namespace {
        try {
            return coreV1Api.replaceNamespace(namespaceName, namespace, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchNamespace(
        namespaceName: String,
        namespace: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Namespace {
        try {
            return coreV1Api.patchNamespace(namespaceName, namespace, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteNamespace(
        namespaceName: String,
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = coreV1Api.deleteNamespaceCall(namespaceName, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getNode(
        nodeName: String,
        pretty: String? = "default",
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1Node {
        try {
            return coreV1Api.readNode(nodeName, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getNodeList(
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1NodeList {
        try {
            return coreV1Api.listNode(includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createNode(
        node: V1Node,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Node {
        try {
            return coreV1Api.createNode(node, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceNode(
        nodeName: String,
        node: V1Node,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Node {
        try {
            return coreV1Api.replaceNode(nodeName, node, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchNode(
        nodeName: String,
        node: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Node {
        try {
            return coreV1Api.patchNode(nodeName, node, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteNode(
        nodeName: String,
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = coreV1Api.deleteNodeCall(nodeName, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getPod(
        podName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1Pod {
        try {
            return coreV1Api.readNamespacedPod(podName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getPodList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1PodList {
        try {
            return coreV1Api.listNamespacedPod(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getPodListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1PodList {
        try {
            return coreV1Api.listPodForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getPodLog(
        podName: String,
        namespace: String? = "default",
        container: String? = null,
        follow: Boolean? = null,
        limitBytes: Int? = null,
        pretty: String? = null,
        previous: Boolean? = null,
        sinceSeconds: Int? = null,
        tailLines: Int? = null,
        timestamps: Boolean? = null
    ): String {
        try {
            return coreV1Api.readNamespacedPodLog(podName, namespace, container, follow, limitBytes, pretty, previous, sinceSeconds, tailLines, timestamps)
        } catch (e: Exception) {
            throw throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createPod(
        namespace: String? = "default",
        pod: V1Pod,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Pod {
        try {
            return coreV1Api.createNamespacedPod(namespace, pod, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replacePod(
        podName: String,
        namespace: String? = "default",
        pod: V1Pod,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Pod {
        try {
            return coreV1Api.replaceNamespacedPod(podName, namespace, pod, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchPod(
        podName: String,
        namespace: String? = "default",
        pod: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Pod {
        try {
            return coreV1Api.patchNamespacedPod(podName, namespace, pod, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deletePod(
        podName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = coreV1Api.deleteNamespacedPodCall(podName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getReplicaSet(
        replicaSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1ReplicaSet {
        try {
            return appsV1Api.readNamespacedReplicaSet(replicaSetName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getReplicaSetList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1ReplicaSetList {
        try {
            return appsV1Api.listNamespacedReplicaSet(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getReplicaSetListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1ReplicaSetList {
        try {
            return appsV1Api.listReplicaSetForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createReplicaSet(
        namespace: String? = "default",
        replicaSet: V1ReplicaSet,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1ReplicaSet {
        try {
            return appsV1Api.createNamespacedReplicaSet(namespace, replicaSet, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceReplicaSet(
        replicaSetName: String,
        namespace: String? = "default",
        replicaSet: V1ReplicaSet,
        pretty: String? = null,
        dryRun: String? = null
    ): V1ReplicaSet {
        try {
            return appsV1Api.replaceNamespacedReplicaSet(replicaSetName, namespace, replicaSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchReplicaSet(
        replicaSetName: String,
        namespace: String? = "default",
        replicaSet: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1ReplicaSet {
        try {
            return appsV1Api.patchNamespacedReplicaSet(replicaSetName, namespace, replicaSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteReplicaSet(
        replicaSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = appsV1Api.deleteNamespacedReplicaSetCall(replicaSetName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getSecret(
        secretName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1Secret {
        try {
            return coreV1Api.readNamespacedSecret(secretName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getSecretList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1SecretList {
        try {
            return coreV1Api.listNamespacedSecret(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getSecretListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1SecretList {
        try {
            return coreV1Api.listSecretForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createSecret(
        namespace: String? = "default",
        secret: V1Secret,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Secret {
        try {
            return coreV1Api.createNamespacedSecret(namespace, secret, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceSecret(
        secretName: String,
        namespace: String? = "default",
        secret: V1Secret,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Secret {
        try {
            return coreV1Api.replaceNamespacedSecret(secretName, namespace, secret, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchSecret(
        secretName: String,
        namespace: String? = "default",
        secret: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Secret {
        try {
            return coreV1Api.patchNamespacedSecret(secretName, namespace, secret, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteSecret(
        secretName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = coreV1Api.deleteNamespacedSecretCall(secretName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getService(
        serviceName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1Service {
        try {
            return coreV1Api.readNamespacedService(serviceName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getServiceList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1ServiceList {
        try {
            return coreV1Api.listNamespacedService(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getServiceListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1ServiceList {
        try {
            return coreV1Api.listServiceForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createService(
        namespace: String? = "default",
        service: V1Service,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Service {
        try {
            return coreV1Api.createNamespacedService(namespace, service, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceService(
        serviceName: String,
        namespace: String? = "default",
        service: V1Service,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Service {
        try {
            return coreV1Api.replaceNamespacedService(serviceName, namespace, service, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchService(
        serviceName: String,
        namespace: String? = "default",
        service: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1Service {
        try {
            return coreV1Api.patchNamespacedService(serviceName, namespace, service, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteService(
        serviceName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = coreV1Api.deleteNamespacedServiceCall(serviceName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }

    fun getStatefulSet(
        statefulSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        exact: Boolean? = null,
        export: Boolean? = null
    ): V1StatefulSet {
        try {
            return appsV1Api.readNamespacedStatefulSet(statefulSetName, namespace, pretty, exact, export)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getStatefulSetList(
        namespace: String? = "default",
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        _continue: String? = null,
        fieldSelector: String? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1StatefulSetList {
        try {
            return appsV1Api.listNamespacedStatefulSet(namespace, includeUninitialized, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun getStatefulSetListForAllNamespaces(
        _continue: String? = null,
        fieldSelector: String? = null,
        includeUninitialized: Boolean? = null,
        labelSelector: String? = null,
        limit: Int? = null,
        pretty: String? = null,
        resourceVersion: String? = null,
        timeoutSeconds: Int? = null,
        watch: Boolean? = null
    ): V1StatefulSetList {
        try {
            return appsV1Api.listStatefulSetForAllNamespaces(_continue, fieldSelector, includeUninitialized, labelSelector, limit, pretty, resourceVersion, timeoutSeconds, watch)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun createStatefulSet(
        namespace: String? = "default",
        statefulSet: V1StatefulSet,
        includeUninitialized: Boolean? = null,
        pretty: String? = null,
        dryRun: String? = null
    ): V1StatefulSet {
        try {
            return appsV1Api.createNamespacedStatefulSet(namespace, statefulSet, includeUninitialized, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun replaceStatefulSet(
        statefulSetName: String,
        namespace: String? = "default",
        statefulSet: V1StatefulSet,
        pretty: String? = null,
        dryRun: String? = null
    ): V1StatefulSet {
        try {
            return appsV1Api.replaceNamespacedStatefulSet(statefulSetName, namespace, statefulSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun patchStatefulSet(
        statefulSetName: String,
        namespace: String? = "default",
        statefulSet: Any,
        pretty: String? = null,
        dryRun: String? = null
    ): V1StatefulSet {
        try {
            return appsV1Api.patchNamespacedStatefulSet(statefulSetName, namespace, statefulSet, pretty, dryRun)
        } catch (e: Exception) {
            throw createKubernetesApiException(e.message, e.cause)
        }
    }

    fun deleteStatefulSet(
        statefulSetName: String,
        namespace: String? = "default",
        pretty: String? = null,
        deleteOptions: V1DeleteOptions? = null,
        dryRun: String? = null,
        gracePeriodSeconds: Int? = null,
        orphanDependents: Boolean? = null,
        propagationPolicy: String? = null,
        progressListener: ProgressResponseBody.ProgressListener? = null,
        progressRequestListener: ProgressRequestBody.ProgressRequestListener? = null
    ) {
        val response = appsV1Api.deleteNamespacedStatefulSetCall(statefulSetName, namespace, pretty, deleteOptions, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, progressListener, progressRequestListener).execute()

        if (!response.isSuccessful) {
            throw createKubernetesApiException(response.message())
        }
    }
}