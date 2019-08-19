package org.jaram.jubaky.enumuration

enum class Kind {
    DAEMONSET,
    DEPLOYMENT,
    NAMESPACE,
    NODE,
    POD,
    REPLICASET,
    SECRET,
    SERVICE,
    STATEFULSET,
    UNKNOWN;
}

fun toKind(kind: String): Kind {
    return when (kind.toUpperCase()) {
        "DAEMONSET" -> Kind.DAEMONSET
        "DEPLOYMENT" -> Kind.DEPLOYMENT
        "NAMESPACE" -> Kind.NAMESPACE
        "NODE" -> Kind.NODE
        "POD" -> Kind.POD
        "REPLICASET" -> Kind.REPLICASET
        "SECRET" -> Kind.SECRET
        "SERVICE" -> Kind.SERVICE
        "STATEFULSET" -> Kind.STATEFULSET
        else -> Kind.UNKNOWN
    }
}