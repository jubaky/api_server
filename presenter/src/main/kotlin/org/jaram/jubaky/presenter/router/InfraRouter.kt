package org.jaram.jubaky.presenter.router

import io.ktor.routing.*
import org.jaram.jubaky.presenter.ext.*
import org.jaram.jubaky.service.KubernetesService

fun Route.infra(kubernetesService: KubernetesService) {
    get("/") {
        response(
            "Kubernetes Service"
        )
    }

    route("/daemonSet") {
        get("/") {
            response(
                kubernetesService.getDaemonSetList()
            )
        }

        get("/{namespace}/{name}") {
            val daemonSetName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getDaemonSet(daemonSetName, namespaceName)
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getDaemonSetList(namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createDaemonSet(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceDaemonSet(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val daemonSetName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteDaemonSet(daemonSetName, namespaceName)
            )
        }
    }

    route("/deployment") {
        get("/") {
            response(
                kubernetesService.getDeploymentList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getDeploymentList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val deploymentName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getDeployment(deploymentName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createDeployment(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceDeployment(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val deploymentName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteDeployment(deploymentName, namespaceName)
            )
        }
    }

    route("/namespace") {
        get("/") {
            response(
                kubernetesService.getNamespaceList()
            )
        }

        get("/{name}") {
            val namespaceName = pathParam("name", "default")

            response(
                kubernetesService.getNamespace(namespaceName)
            )
        }

        post("/") {
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createNamespace(yaml)
            )
        }

        put("/{name}") {
            val namespaceName = pathParam("name", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceNamespace(namespaceName, yaml)
            )
        }

        delete("/{name}") {
            val namespaceName = pathParam("name", "default")

            response(
                kubernetesService.deleteNamespace(namespaceName)
            )
        }
    }

    route("/node") {
        get("/") {
            response(
                kubernetesService.getNodeList()
            )
        }

        get("/{name}") {
            val nodeName = pathParam("name")

            response(
                kubernetesService.getNode(nodeName)
            )
        }
    }

    route("/pod") {
        get("/") {
            response(
                kubernetesService.getPodList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getPodList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val podName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getPod(podName, namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val podName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getPod(podName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createPod(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replacePod(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val podName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deletePod(podName, namespaceName)
            )
        }
    }

    route("/replicaSet") {
        get("/") {
            response(
                kubernetesService.getReplicaSetList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getReplicaSetList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val replicaSetName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getReplicaSet(replicaSetName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createReplicaSet(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceReplicaSet(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val replicaSetName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteReplicaSet(replicaSetName, namespaceName)
            )
        }
    }

    route("/secret") {
        get("/") {
            response(
                kubernetesService.getSecretList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getSecretList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val secretName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getSecret(secretName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createSecret(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceSecret(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val secretName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteSecret(secretName, namespaceName)
            )
        }
    }

    route("/service") {
        get("/") {
            response(
                kubernetesService.getServiceList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getServiceList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val serviceName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getService(serviceName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createService(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceService(objectName, yaml, namespaceName)
            )
        }

        delete("/{namespace}/{name}") {
            val serviceName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteService(serviceName, namespaceName)
            )
        }
    }

    route("/statefulSet") {
        get("/") {
            response(
                kubernetesService.getStatefulSetList()
            )
        }

        get("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getStatefulSetList(namespaceName)
            )
        }

        get("/{namespace}/{name}") {
            val statefulSetName = pathParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.getStatefulSet(statefulSetName, namespaceName)
            )
        }

        post("/{namespace}") {
            val namespaceName = pathParam("namespace", "default")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.createStatefulSet(yaml, namespaceName)
            )
        }

        put("/{namespace}/{name}") {
            val namespaceName = pathParam("namespace", "default")
            val objectName = pathParam("name")
            val yaml = bodyParam("yaml")

            response(
                kubernetesService.replaceStatefulSet(objectName, yaml, namespaceName)
            )
        }

        delete("/") {
            val statefulSetName = queryParam("name")
            val namespaceName = pathParam("namespace", "default")

            response(
                kubernetesService.deleteStatefulSet(statefulSetName, namespaceName)
            )
        }
    }
}