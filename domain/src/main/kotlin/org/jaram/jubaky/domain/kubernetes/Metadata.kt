package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Metadata(
    val annotations: Map<String, String>?,
    val clusterName: String?,
    val creationTimestamp: DateTime?,
    val deletionGracePeriodSeconds: Long?,
    val deletionTimestamp: DateTime?,
    val finalizers: List<String>?,
    val generateName: String?,
    val generation: Long?,
    val initializers: Initializers?,
    val labels: Map<String, String>?,
    val name: String?,
    val namespace: String?,
    val ownerReference: List<OwnerReference>?,
    val resourceVersion: String?,
    val uid: String?
) {
    data class Initializers(
        val pending: List<Initializer>?,
        val result: Status?
    ) {
        data class Initializer(
            val name: String?
        )
    }

    data class OwnerReference(
        val apiVersion: String?,
        val blockOwnerDeletion: Boolean?,
        val controller: Boolean?,
        val kind: String?,
        val name: String?,
        val uid: String?
    )
}