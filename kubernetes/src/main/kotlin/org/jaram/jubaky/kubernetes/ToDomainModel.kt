package org.jaram.jubaky.kubernetes

import io.kubernetes.client.custom.IntOrString
import io.kubernetes.client.custom.Quantity
import io.kubernetes.client.models.*
import org.jaram.jubaky.domain.kubernetes.*

fun V1Container.toDomainModel(): Container = Container(
    args = this.args,
    command = this.command,
    image = this.image,
    name  = this.name,
    containerPorts = this.ports?.map { port -> port.toDomainModel() },
    resources = this.resources?.toDomainModel(),
    terminationMessagePath = this.terminationMessagePath,
    terminationMessagePolicy = this.terminationMessagePolicy,
    tty = this.isTty,
    volumeDevices = this.volumeDevices?.map { volumeDevice -> volumeDevice.toDomainModel() },
    volumeMounts = this.volumeMounts?.map { volumeMount -> volumeMount.toDomainModel() },
    workingDir = this.workingDir
)

fun V1ContainerImage.toDomainModel(): Container.ContainerImage = Container.ContainerImage(
    names = this.names,
    sizeBytes = this.sizeBytes
)

fun V1ContainerPort.toDomainModel(): Container.ContainerPort = Container.ContainerPort(
    containerPort = this.containerPort,
    hostIP = this.hostIP,
    hostPort = this.hostPort,
    name = this.name,
    protocol = this.protocol
)

fun V1ContainerStatus.toDomainModel(): Container.ContainerStatus = Container.ContainerStatus(
    containerID = this.containerID,
    image = this.image,
    imageID = this.imageID,
    lastState = this.lastState?.toDomainModel(),
    name = this.name,
    ready = this.isReady,
    restartCount = this.restartCount,
    state = this.state?.toDomainModel()
)

fun V1ContainerState.toDomainModel(): Container.ContainerState = Container.ContainerState(
    running = this.running?.toDoaminModel(),
    terminated = this.terminated?.toDomainModel(),
    waiting = this.waiting?.toDoaminModel()
)

fun V1ContainerStateRunning.toDoaminModel(): Container.ContainerState.ContainerStateRunning = Container.ContainerState.ContainerStateRunning(
    startedAt = this.startedAt
)

fun V1ContainerStateTerminated.toDomainModel(): Container.ContainerState.ContainerStateTerminated = Container.ContainerState.ContainerStateTerminated(
    containerID = this.containerID,
    exitCode = this.exitCode,
    finishedAt = this.finishedAt,
    message = this.message,
    reason = this.reason,
    signal = this.signal,
    startedAt = this.startedAt
)

fun V1ContainerStateWaiting.toDoaminModel(): Container.ContainerState.ContainerStateWaiting = Container.ContainerState.ContainerStateWaiting(
    message = this.message,
    reason = this.reason
)

fun V1beta1DaemonSet.toDomainModel(): DaemonSet = DaemonSet(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = DaemonSet.DaemonSetSpec(
        revisionHistoryLimit = spec.revisionHistoryLimit,
        selector = spec.selector?.toDomainModel(),
        template = spec.template?.toDomainModel(),
        templateGeneration = spec.templateGeneration,
        updateStrategy = DaemonSet.DaemonSetUpdateStrategy(
            rollingUpdate = DaemonSet.DaemonSetRollingUpdate(
                maxUnavailable = spec.updateStrategy?.rollingUpdate?.maxUnavailable?.getValue()
            ),
            type = spec.updateStrategy?.type
        )
    ),
    status = DaemonSet.DaemonSetStatus(
        collisionCount = status.collisionCount,
        conditions = status.conditions?.map { condition -> condition.toDomainModel() },
        currentNumberScheduled = status.currentNumberScheduled,
        desiredNumberScheduled = status.desiredNumberScheduled,
        numberAvailable = status.numberAvailable,
        numberMisscheduled = status.numberMisscheduled,
        numberReady = status.numberReady,
        numberUnavailable = status.numberUnavailable,
        observedGeneration = status.observedGeneration,
        updatedNumberScheduled = status.updatedNumberScheduled
    )
)

fun V1beta1DaemonSetCondition.toDomainModel(): DaemonSet.DaemonSetCondition = DaemonSet.DaemonSetCondition(
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun ExtensionsV1beta1Deployment.toDomainModel(): Deployment = Deployment(
    apiVersion = apiVersion,
    kind= kind,
    metadata = metadata?.toDomainModel(),
    spec = Deployment.DeploymentSpec(
        progressDeadlineSeconds = spec.progressDeadlineSeconds,
        replicas = spec.replicas,
        revisionHistoryLimit = spec.revisionHistoryLimit,
        selector = spec.selector.toDomainModel(),
        strategy = Deployment.DeploymentStrategy(
            rollingUpdate = Deployment.DeploymentRollingUpdate(
                maxSurge = spec.strategy?.rollingUpdate?.maxSurge,
                maxUnavailable = spec.strategy?.rollingUpdate?.maxUnavailable?.toInt() ?: 0
            ),
            type = spec.strategy?.type
        ),
        template = spec.template?.toDomainModel()
    ),
    status = Deployment.DeploymentStatus(
        availableReplicas = status.availableReplicas,
        collisionCount = status.collisionCount,
        conditions = status.conditions?.map { condition -> condition.toDomainModel() },
        observedGeneration = status.observedGeneration,
        readyReplicas = status.readyReplicas ?: 0,
        replicas = status.replicas ?: 0,
        unavailableReplicas = status.unavailableReplicas ?: 0,
        updatedReplicas = status.updatedReplicas ?: 0
    )
)

fun ExtensionsV1beta1DeploymentCondition.toDomainModel(): Deployment.DeploymentCondition = Deployment.DeploymentCondition(
    lastTransitionTime = this.lastTransitionTime,
    lastUpdateTime = this.lastUpdateTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1HostAlias.toDomainModel(): Pod.HostAlias = Pod.HostAlias(
    hostnames = this.hostnames,
    ip = this.ip
)

fun V1HostPathVolumeSource.toDomainModel(): Volume.HostPathVolumeSource = Volume.HostPathVolumeSource(
    path = this.path,
    type = this.type
)

fun V1Initializer.toDomainModel(): Metadata.Initializers.Initializer = Metadata.Initializers.Initializer(
    name = this.name
)

fun V1Initializers.toDomainModel(): Metadata.Initializers = Metadata.Initializers(
    pending = this.pending?.map { initializer -> initializer.toDomainModel() },
    result = this.result?.toDomainModel()
)

fun V1LabelSelector.toDomainModel(): Selector = Selector(
    matchExpressions = this.matchExpressions?.map { matchExpression -> matchExpression.toDomainModel() },
    matchLabels = this.matchLabels
)

fun V1LabelSelectorRequirement.toDomainModel(): Selector.SelectorRequirements = Selector.SelectorRequirements(
    key = this.key,
    operator = this.operator,
    values = this.values
)

fun V1ListMeta.toDomainModel(): Status.ListMeta {
    return Status.ListMeta(
        _continue = this.`continue`,
        resourceVersion = this.resourceVersion,
        selfLink = this.selfLink
    )
}

fun V1LoadBalancerIngress.toDomainModel(): Service.LoadBalancerIngress = Service.LoadBalancerIngress(
    hostname = this.hostname,
    ip = this.ip
)

fun V1Namespace.toDomainModel(): Namespace = Namespace(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = Namespace.NamespaceSpec(
        finalizers = spec.finalizers
    ),
    status = Namespace.NamespaceStatus(
        phase = status.phase
    )
)

fun V1Node.toDomainModel(): Node {
    val allocatableMap = mutableMapOf<String, Resource.Quantity>()
    val capacityMap = mutableMapOf<String, Resource.Quantity>()

    for (allocatable in status.allocatable?: mapOf()) {
        allocatableMap[allocatable.key] = allocatable.value.toDomainModel()
    }

    for (capacity in status.capacity?: mapOf()) {
        capacityMap[capacity.key] = capacity.value.toDomainModel()
    }

    return Node(
        apiVersion = apiVersion,
        kind = kind,
        metadata = metadata?.toDomainModel(),
        spec = Node.NodeSpec(
            configSource = Node.NodeConfigSource(
                configMap = Node.ConfigMapNodeConfigSource(
                    kubeletConfigKey = spec.configSource?.configMap?.kubeletConfigKey,
                    name = spec.configSource?.configMap?.name,
                    namespace = spec.configSource?.configMap?.namespace,
                    resourceVersion = spec.configSource?.configMap?.resourceVersion,
                    uid = spec.configSource?.configMap?.uid
                )
            ),
            externalID = spec.externalID,
            podCIDR = spec.podCIDR,
            providerID = spec.providerID,
            taints = spec.taints?.map { taint -> taint.toDomainModel() },
            unschedulable = spec.isUnschedulable
        ),
        status = Node.NodeStatus(
            addresses = status.addresses?.map { address -> address.toDomainModel() },
            allocatable = allocatableMap,
            capacity = capacityMap,
            conditions = status.conditions?.map { condition -> condition.toDomainModel() },
            daemonEndpoints = Node.NodeDaemonEndpoints(
                kubeletEndpoint = Node.DaemonEndpoint(
                    Port = status.daemonEndpoints?.kubeletEndpoint?.port
                )
            ),
            images = status.images?.map { image -> image.toDomainModel() },
            nodeInfo = Node.NodeSystemInfo(
                architecture = status.nodeInfo?.architecture,
                bootID = status.nodeInfo?.bootID,
                containerRuntimeVersion = status.nodeInfo?.containerRuntimeVersion,
                kernelVersion = status.nodeInfo?.kernelVersion,
                kubeProxyVersion = status.nodeInfo?.kubeProxyVersion,
                kubeletVersion = status.nodeInfo?.kubeletVersion,
                machineID = status.nodeInfo?.machineID,
                operatingSystem = status.nodeInfo?.operatingSystem,
                osImage = status.nodeInfo?.osImage,
                systemUUID = status.nodeInfo?.systemUUID
            ),
            phase = status.phase
        )
    )
}

fun V1NodeAddress.toDomainModel(): Node.NodeAddress = Node.NodeAddress(
    address = this.address,
    type = this.type
)

fun V1NodeCondition.toDomainModel(): Node.NodeCondition = Node.NodeCondition(
    lastHeartbeatTime = this.lastHeartbeatTime,
    lastTransitionTime = this.lastTransitionTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1ObjectMeta.toDomainModel(): Metadata = Metadata(
    annotations = this.annotations?: mapOf(),
    clusterName = this.clusterName,
    creationTimestamp = this.creationTimestamp,
    deletionGracePeriodSeconds = this.deletionGracePeriodSeconds,
    deletionTimestamp = this.deletionTimestamp,
    finalizers = this.finalizers,
    generateName = this.generateName,
    generation = this.generation,
    initializers = this.initializers?.toDomainModel(),
    labels = this.labels?: mapOf(),
    name = this.name,
    namespace = this.namespace,
    ownerReference = this.ownerReferences?.map { ownerReference -> ownerReference.toDomainModel() },
    resourceVersion = this.resourceVersion,
    uid = this.uid
)

fun V1OwnerReference.toDomainModel(): Metadata.OwnerReference = Metadata.OwnerReference(
    apiVersion = this.apiVersion,
    blockOwnerDeletion = this.isBlockOwnerDeletion,
    controller = this.isController,
    kind = this.kind,
    name = this.name,
    uid = this.uid
)

fun V1PersistentVolumeClaim.toDomainModel(): PersistentVolumeClaim {
    val resourceLimitsMap = mutableMapOf<String, Resource.Quantity>()
    val resourceRequestsMap = mutableMapOf<String, Resource.Quantity>()
    val capacityMap = mutableMapOf<String, Resource.Quantity>()

    for (resourceLimit in this.spec.resources?.limits?: mapOf()) {
        resourceLimitsMap[resourceLimit.key] = resourceLimit.value.toDomainModel()
    }

    for (resourceRequest in this.spec.resources?.requests?: mapOf()) {
        resourceRequestsMap[resourceRequest.key] = resourceRequest.value.toDomainModel()
    }

    for (capacity in this.status.capacity?: mapOf()) {
        capacityMap[capacity.key] = capacity.value.toDomainModel()
    }

    return PersistentVolumeClaim(
        apiVersion = this.apiVersion,
        kind = this.kind,
        metadata = this.metadata?.toDomainModel(),
        spec = PersistentVolumeClaim.PersistentVolumeClaimSpec(
            accessModes = this.spec.accessModes,
            dataSource = PersistentVolumeClaim.TypedLocalObjectReference(
                apiGroup = this.spec.dataSource?.apiGroup,
                kind = this.spec.dataSource?.kind,
                name = this.spec.dataSource?.name
            ),
            resources = Resource(
                limits = resourceLimitsMap,
                requests = resourceRequestsMap
            ),
            selector = this.spec.selector.toDomainModel(),
            storageClassName = this.spec.storageClassName,
            volumeMode = this.spec.volumeMode,
            volumeName = this.spec.volumeName
        ),
        status = PersistentVolumeClaim.PersistentVolumeClaimStatus(
            accessModes = this.status.accessModes,
            capacity = capacityMap,
            conditions = this.status.conditions?.map { condition -> condition.toDoaminModel() },
            phase = this.status.phase
        )
    )
}

fun V1PersistentVolumeClaimCondition.toDoaminModel(): PersistentVolumeClaim.PersistentVolumeClaimCondition = PersistentVolumeClaim.PersistentVolumeClaimCondition(
    lastProbeTime = this.lastProbeTime,
    lastTransitionTime = this.lastTransitionTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1Pod.toDomainModel(): Pod = Pod(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = spec?.toDomainModel(),
    status = Pod.PodStatus(
        conditions = status.conditions?.map { condition -> condition.toDomainModel() },
        containerStatuses = status.containerStatuses?.map { containerStatus -> containerStatus.toDomainModel() },
        hostIP = status.hostIP,
        initContainerStatuses = status.initContainerStatuses?.map { initContainerStatus -> initContainerStatus.toDomainModel() },
        message = status.message,
        nominatedNodeName = status.nominatedNodeName,
        phase = status.phase,
        podIP = status.podIP,
        qosClass = status.qosClass,
        reason = status.reason,
        startTime = status.startTime
    )
)

fun V1PodCondition.toDomainModel(): Pod.PodCondition = Pod.PodCondition(
    lastProbeTime = this.lastProbeTime,
    lastTransitionTime = this.lastTransitionTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1PodDNSConfig.toDomainModel(): Pod.PodDNSConfig = Pod.PodDNSConfig(
    nameservers = this.nameservers,
    options = this.options?.map { option -> option.toDomainModel() },
    searches = this.searches
)

fun V1PodDNSConfigOption.toDomainModel(): Pod.PodDNSConfigOption = Pod.PodDNSConfigOption(
    name = this.name,
    value = this.value
)

fun V1PodSecurityContext.toDomainModel(): Pod.PodSecurityContext = Pod.PodSecurityContext(
    fsGroup = this.fsGroup,
    runAsGroup = this.runAsGroup,
    runAsNonRoot = this.isRunAsNonRoot,
    runAsUser = this.runAsUser
)

fun V1PodSpec.toDomainModel(): Pod.PodSpec = Pod.PodSpec(
    containers = this.containers?.map { container -> container.toDomainModel() },
    dnsConfig = this.dnsConfig?.toDomainModel(),
    dnsPolicy = this.dnsPolicy,
    enableServiceLinks = this.isEnableServiceLinks,
    hostAliases = this.hostAliases?.map { hostAlias -> hostAlias.toDomainModel() },
    hostIPC = this.isHostIPC,
    hostNetwork = this.isHostNetwork,
    hostPID = this.isHostPID,
    hostname = this.hostname,
    initContainers = this.initContainers?.map { container -> container.toDomainModel()},
    nodeName = this.nodeName,
    nodeSelector = this.nodeSelector,
    priority = this.priority,
    priorityClassName = this.priorityClassName,
    restartPolicy = this.restartPolicy,
    schedulerName = this.schedulerName,
    securityContext = this.securityContext?.toDomainModel(),
    serviceAccount = this.serviceAccount,
    serviceAccountName = this.serviceAccountName,
    shareProcessNamespace = this.isShareProcessNamespace,
    subdomain = this.subdomain,
    terminationGracePeriodSeconds = this.terminationGracePeriodSeconds,
    tolerations = this.tolerations?.map { toleration -> toleration.toDomainModel() },
    volumes = this.volumes?.map { volume -> volume.toDomainModel() }
)

fun V1PodTemplateSpec.toDomainModel(): PodTemplate.PodTemplateSpec = PodTemplate.PodTemplateSpec(
    metadata = this.metadata?.toDomainModel(),
    spec = this.spec?.toDomainModel()
)

fun Quantity.toDomainModel(): Resource.Quantity = Resource.Quantity(
    number = this.number,
    format = this.format.base
)

fun V1ReplicaSet.toDomainModel(): ReplicaSet = ReplicaSet(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = ReplicaSet.ReplicaSetSpec(
        minReadySeconds = spec.minReadySeconds,
        replicas = spec.replicas,
        selector = spec.selector?.toDomainModel(),
        template = spec.template?.toDomainModel()
    ),
    status = ReplicaSet.ReplicaSetStatus(
        availableReplicas = status.availableReplicas,
        conditions = status.conditions?.map { condition -> condition.toDomainModel() },
        fullyLabeledReplicas = status.fullyLabeledReplicas,
        observedGeneration = status.observedGeneration,
        readyReplicas = status.readyReplicas,
        replicas = status.replicas
    )
)

fun V1ReplicaSetCondition.toDomainModel(): ReplicaSet.ReplicaSetCondition = ReplicaSet.ReplicaSetCondition(
    lastTransitionTime = this.lastTransitionTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1ResourceRequirements.toDomainModel(): Resource {
    val resourceLimitsMap = mutableMapOf<String, Resource.Quantity>()
    val resourceRequestsMap = mutableMapOf<String, Resource.Quantity>()

    for (resourceLimit in this.limits ?: mapOf()) {
        resourceLimitsMap[resourceLimit.key] = resourceLimit.value.toDomainModel()
    }

    for (resourceRequest in this.requests ?: mapOf()) {
        resourceRequestsMap[resourceRequest.key] = resourceRequest.value.toDomainModel()
    }

    return Resource(
        limits = resourceLimitsMap,
        requests = resourceRequestsMap
    )
}

fun V1Secret.toDomainModel(): Secret = Secret(
    apiVersion = apiVersion,
    data = data,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    stringData = stringData,
    type = type
)

fun V1Service.toDomainModel(): Service = Service(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = Service.ServiceSpec(
        clusterIP = spec.clusterIP,
        externalIPs = spec.externalIPs,
        externalName = spec.externalName,
        externalTrafficPolicy = spec.externalTrafficPolicy,
        healthCheckNodePort = spec.healthCheckNodePort,
        loadBalancerIP = spec.loadBalancerIP,
        loadBalancerSourceRanges = spec.loadBalancerSourceRanges,
        ports = spec.ports?.map { spec -> spec.toDomainModel() },
        publishNotReadyAddresses = spec.isPublishNotReadyAddresses,
        selector = spec.selector,
        sessionAffinity = spec.sessionAffinity,
        sessionAffinityConfig = Service.SessionAffinityConfig(
            clientIP = Service.ClientIPConfig(
                timeoutSeconds = spec.sessionAffinityConfig?.clientIP?.timeoutSeconds
            )
        ),
        type = spec.type
    ),
    status = Service.ServiceStatus(
        loadBalancer = Service.LoadBalancerStatus(
            ingress = status.loadBalancer?.ingress?.map { ingress -> ingress.toDomainModel() }
        )
    )
)

fun V1ServicePort.toDomainModel(): Service.ServicePort = Service.ServicePort(
    name = this.name,
    nodePort = this.nodePort,
    port = this.port,
    protocol = this.protocol,
    targetPort = this.targetPort?.getValue()
)

fun V1StatefulSet.toDomainModel(): StatefulSet = StatefulSet(
    apiVersion = apiVersion,
    kind = kind,
    metadata = metadata?.toDomainModel(),
    spec = StatefulSet.StatefulSetSpec(
        podManagementPolicy = spec.podManagementPolicy,
        replicas = spec.replicas,
        revisionHistoryLimit = spec.revisionHistoryLimit,
        selector = spec.selector?.toDomainModel(),
        serviceName = spec.serviceName,
        template = spec.template?.toDomainModel(),
        updateStrategy = StatefulSet.StatefulSetUpdateStrategy(
            rollingUpdate = StatefulSet.RollingUpdateStatefulSetStrategy(
                partition = spec.updateStrategy?.rollingUpdate?.partition
            ),
            type = spec?.updateStrategy?.type
        ),
        volumeClaimTemplates = spec.volumeClaimTemplates?.map { volumeClaimTemplate -> volumeClaimTemplate.toDomainModel() }
    ),
    status = StatefulSet.StatefulSetStatus(
        collisionCount = status.collisionCount,
        conditions = status.conditions?.map { condition -> condition.toDomainModel() },
        currentReplicas = status.currentReplicas,
        currentRevision = status.currentRevision,
        observedGeneration = status.observedGeneration,
        readyReplicas = status.readyReplicas,
        replicas = status.replicas,
        updateRevision = status.updateRevision,
        updatedReplicas = status.updatedReplicas
    )
)

fun V1StatefulSetCondition.toDomainModel(): StatefulSet.StatefulSetCondition = StatefulSet.StatefulSetCondition(
    lastTransitionTime = this.lastTransitionTime,
    message = this.message,
    reason = this.reason,
    status = this.status,
    type = this.type
)

fun V1Status.toDomainModel(): Status = Status(
    apiVersion = this.apiVersion,
    code = this.code,
    details = this.details?.toDomainModel(),
    kind = this.kind,
    message = this.message,
    metadata = this.metadata?.toDomainModel(),
    reason = this.reason,
    status = this.status
)

fun V1StatusCause.toDomainModel(): Status.StatusCause = Status.StatusCause(
    field = this.field,
    message = this.message,
    reason = this.reason
)

fun V1StatusDetails.toDomainModel(): Status.StatusDetails = Status.StatusDetails(
    causes = this.causes?.map { cause -> cause.toDomainModel() },
    group = this.group,
    kind = this.kind,
    name = this.name,
    retryAfterSeconds = this.retryAfterSeconds,
    uid = this.uid
)

fun V1Taint.toDomainModel(): Taint = Taint(
    effect = this.effect,
    key = this.key,
    timeAdded = this.timeAdded,
    value = this.value
)

fun V1Toleration.toDomainModel(): Pod.Toleration = Pod.Toleration(
    effect = this.effect,
    key = this.key,
    operator = this.operator,
    tolerationSeconds = this.tolerationSeconds,
    value = this.value
)

fun V1Volume.toDomainModel(): Volume = Volume(
    name = this.name,
    hostPath = this.hostPath?.toDomainModel()
)

fun V1VolumeDevice.toDomainModel(): Volume.VolumeDevice = Volume.VolumeDevice(
    devicePath = this.devicePath,
    name = this.name
)

fun V1VolumeMount.toDomainModel(): Volume.VolumeMount = Volume.VolumeMount(
    mountPath = this.mountPath,
    mountPropagation = this.mountPropagation,
    name = this.name,
    readOnly = this.isReadOnly,
    subPath = this.subPath
)

private fun IntOrString.getValue(): Any {
    return if (this.isInteger)
        this.intValue
    else
        this.strValue
}