package org.jaram.jubaky.service

import com.google.common.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jaram.jubaky.KuberenetesObjectNotFoundException
import org.jaram.jubaky.KubernetesBuildDuplicationException
import org.jaram.jubaky.domain.checker.Deploy
import org.jaram.jubaky.domain.checker.toDeploy
import org.jaram.jubaky.enumuration.DeployStatus
import org.jaram.jubaky.enumuration.Kind
import org.jaram.jubaky.repository.KubernetesRepository
import java.util.concurrent.TimeUnit

class DeployCheckService(
    private val intervalDelayTime: Int,
    private val intervalCheckHealthTime: Int
) {

    lateinit var kubernetesRepository: KubernetesRepository

    val deployEventBus = EventBus("DeployEventBus")

    private val progressDeployList = ArrayList<Deploy>()
    private val successDeployList = ArrayList<Deploy>()
    private val failureDeployList = ArrayList<Deploy>()

    private lateinit var deployCheckJob: Job
    private lateinit var checkHealthJob: Job

    fun runDeployCheck() {
        deployCheckJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val successDeployIdxList = mutableListOf<Int>()
                val failureDeployIdxList = mutableListOf<Int>()

                for (i in 0 until progressDeployList.size) {
                    val deploy = progressDeployList[i]
                    var obj: Deploy

                    when (deploy.kind) {
                        Kind.DAEMONSET -> TODO()
                        Kind.DEPLOYMENT ->
                            obj = toDeploy(kubernetesRepository.getDeployment(deploy.name, deploy.namespace), DeployStatus.UNKNOWN)
                        Kind.NAMESPACE -> TODO()
                        Kind.NODE -> TODO()
                        Kind.POD -> TODO()
                        Kind.REPLICASET -> TODO()
                        Kind.SECRET -> TODO()
                        Kind.SERVICE -> TODO()
                        Kind.STATEFULSET -> TODO()
                        Kind.UNKNOWN -> throw KuberenetesObjectNotFoundException()
                    }

                    // Handling status
                    if (obj.updatedReplicas - obj.readyReplicas > 0) {  // PROGRESS
                        obj.status = DeployStatus.PROGRESS
                    } else {
                        if (obj.unavailableReplicas - obj.maxUnavailable > 0) {  // FAILURE
                            obj.status = DeployStatus.FAILURE
                        } else {  // SUCCESS
                            obj.status = DeployStatus.SUCCESS
                        }
                    }

                    when (obj.status) {
                        DeployStatus.SUCCESS -> successDeployIdxList.add(progressDeployList.indexOf(deploy))
                        DeployStatus.FAILURE -> failureDeployIdxList.add(progressDeployList.indexOf(deploy))
                        DeployStatus.PROGRESS -> {}
                        DeployStatus.UNKNOWN -> {}  // Throw an Excption ?
                    }

                    progressDeployList[i] = obj
                }

                successDeployIdxList.map { idx -> successDeployList.add(progressDeployList.removeAt(idx)) }
                failureDeployIdxList.map { idx -> failureDeployList.add(progressDeployList.removeAt(idx)) }

                /**
                 * Save data to DB
                 */


                /**
                 * @TODO
                 * Deep clone and doing post
                 */
                deployEventBus.post(
                    mapOf(
                        "progressDeployList" to progressDeployList,
                        "successDeployList" to successDeployList,
                        "failureDeployList" to failureDeployList
                    )
                )

//                println(mapOf(
//                    "progressDeployList" to progressDeployList,
//                    "successDeployList" to successDeployList,
//                    "failureDeployList" to failureDeployList
//                ))

                successDeployList.clear()
                failureDeployList.clear()

                TimeUnit.MILLISECONDS.sleep(intervalDelayTime.toLong())
            }
        }
    }

    fun stopDeployCheck() {
        deployCheckJob.cancel()
    }

    fun runCheckHealth() {
        checkHealthJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (!deployCheckJob.isActive) {
                    runDeployCheck()
                }

                TimeUnit.MILLISECONDS.sleep(intervalCheckHealthTime.toLong())
            }
        }
    }

    fun stopCheckHealth() {
        checkHealthJob.cancel()
    }

    fun getProgressDeployList(): ArrayList<Deploy> {
        return progressDeployList
    }

    fun isDeployCheckJobRunning(): Boolean {
        return deployCheckJob.isActive
    }

    fun checkDeployDuplication(deploy: Deploy): Boolean {
        var isProgress = false

        progressDeployList.map { progressDeploy ->
            if (progressDeploy.name == deploy.name)
                isProgress = true
        }

        if (!isProgress)
            return isProgress
        else
            throw KubernetesBuildDuplicationException()
    }
}