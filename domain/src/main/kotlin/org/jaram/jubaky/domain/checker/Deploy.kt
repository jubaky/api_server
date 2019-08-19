package org.jaram.jubaky.domain.checker

import org.jaram.jubaky.domain.kubernetes.Deployment
import org.jaram.jubaky.enumuration.DeployStatus
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.enumuration.toKind

data class Deploy(
    val name: String,
    val namespace: String,
    val kind: Kind,
    val maxUnavailable: Int,
    val readyReplicas: Int,
    val unavailableReplicas: Int,
    val updatedReplicas: Int,
    var status: DeployStatus
)

fun toDeploy(obj: Any, status: DeployStatus): Deploy {
    return when(obj) {
        is Deployment -> Deploy(
            name = obj.metadata?.name!!,
            namespace = obj.metadata.namespace!!,
            kind = toKind(obj.kind!!),
            maxUnavailable = obj.spec?.strategy?.rollingUpdate?.maxUnavailable!!.toInt(),
            readyReplicas = obj.status?.readyReplicas!!.toInt(),
            unavailableReplicas = obj.status?.unavailableReplicas!!.toInt(),
            updatedReplicas = obj.status?.updatedReplicas!!.toInt(),
            status = status
        )
        /**
         * @TODO
         * DaemonSet, ReplicaSet, Service, StatefulSet 등도 만들기
         */
        else -> Deploy(
            name = "UNKNOWN",
            namespace = "default",
            kind = Kind.UNKNOWN,
            maxUnavailable = 0,
            readyReplicas = 0,
            unavailableReplicas = 0,
            updatedReplicas = 0,
            status = status
        )
    }
}