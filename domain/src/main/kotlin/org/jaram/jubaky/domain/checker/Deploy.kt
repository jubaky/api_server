package org.jaram.jubaky.domain.checker

import org.jaram.jubaky.domain.kubernetes.Deployment
import org.jaram.jubaky.enumuration.DeployStatus
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.toKind
import org.joda.time.DateTime

data class Deploy(
    val deployId: Int,
    val applicationName: String,
    val namespace: String,
    val kind: Kind,
    val maxUnavailable: Int,
    val readyReplicas: Int,
    val unavailableReplicas: Int,
    val updatedReplicas: Int,
    val endTime: DateTime,
    var status: DeployStatus
)

fun toDeploy(deployId: Int, obj: Any, status: DeployStatus): Deploy {
    return when(obj) {
        is Deployment -> Deploy(
            deployId = deployId,
            applicationName = obj.metadata?.name!!,
            namespace = obj.metadata.namespace!!,
            kind = toKind(obj.kind!!),
            maxUnavailable = obj.spec?.strategy?.rollingUpdate?.maxUnavailable!!.toInt(),
            readyReplicas = obj.status?.readyReplicas!!.toInt(),
            unavailableReplicas = obj.status?.unavailableReplicas!!.toInt(),
            updatedReplicas = obj.status?.updatedReplicas!!.toInt(),
            endTime = obj.status?.conditions?.get(0)?.lastUpdateTime!!,
            status = status
        )
        /**
         * @TODO
         * DaemonSet, ReplicaSet, Service, StatefulSet 등도 만들기
         */
        else -> Deploy(
            deployId = deployId,
            applicationName = "UNKNOWN",
            namespace = "default",
            kind = Kind.UNKNOWN,
            maxUnavailable = 0,
            readyReplicas = 0,
            unavailableReplicas = 0,
            updatedReplicas = 0,
            endTime = DateTime(),
            status = status
        )
    }
}