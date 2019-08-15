package org.jaram.jubaky.domain.kubernetes

import org.joda.time.DateTime

data class Container(
    val args: List<String>?,
    val command: List<String>?,
    val image: String?,
    val name: String?,
    val containerPorts: List<ContainerPort>?,
    val resources: Resource?,
    val terminationMessagePath: String?,
    val terminationMessagePolicy: String?,
    val tty: Boolean?,
    val volumeDevices: List<Volume.VolumeDevice>?,
    val volumeMounts: List<Volume.VolumeMount>?,
    val workingDir: String?
) {
    data class ContainerPort(
        val containerPort: Int?,
        val hostIP: String?,
        val hostPort: Int?,
        val name: String?,
        val protocol: String?
    )

    data class ContainerStatus(
        val containerID: String?,
        val image: String?,
        val imageID: String?,
        val lastState: ContainerState?,
        val name: String?,
        val ready: Boolean?,
        val restartCount: Int?,
        val state: ContainerState?
    )

    data class ContainerState(
        val running: ContainerStateRunning?,
        val terminated: ContainerStateTerminated?,
        val waiting: ContainerStateWaiting?
    ) {
        data class ContainerStateRunning(
            val startedAt: DateTime?
        )

        data class ContainerStateTerminated(
            val containerID: String?,
            val exitCode: Int?,
            val finishedAt: DateTime?,
            val message: String?,
            val reason: String?,
            val signal: Int?,
            val startedAt: DateTime?
        )

        data class ContainerStateWaiting(
            val message: String?,
            val reason: String?
        )
    }

    data class ContainerImage(
        val names: List<String>?,
        val sizeBytes: Long?
    )
}