#!/bin/bash
# The script file to run Jubaky-Api-Server

sudo docker build \
    -t jbkcose/jubaky-api-server:dev \
    --build-arg DB_URL=${JUBAKY_DB_URL} \
    --build-arg DB_USERNAME=${JUBAKY_DB_USERNAME} \
    --build-arg DB_PASSWORD=${JUBAKY_DB_PASSWORD} \
    --build-arg TOKEN_SECRET=${JUBAKY_TOKEN_SECRET} \
    --build-arg JENKINS_URL=${JUBAKY_JENKINS_URL} \
    --build-arg JENKINS_USERNAME=${JUBAKY_JENKINS_USERNAME} \
    --build-arg JENKINS_TOKEN=${JUBAKY_JENKINS_TOKEN} \
    --build-arg KUBERNETES_KUBECONFIG_NAME=${JUBAKY_KUBERNETES_KUBECONFIG_NAME} \
    .

# sudo docker pull jbkcose/jubaky-api-server:dev

sudo docker run \
    -d \
    -p 5850:5850 \
    -p 5849:8080 \
    -p 50000:50000 \
    -v ${JUBAKY_JENKINS_CONFIG_PATH}:/var/jenkins_home \
    -v ${JUBAKY_KUBERNETES_KUBECONFIG_PATH}:/var/kubernetes_home \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v /usr/bin/docker:/usr/bin/docker \
    -u root \
    --privileged \
    --name jubaky-api-server \
    --restart always \
    jbkcose/jubaky-api-server:dev

# sudo docker run \
#     -p 5850:5850 \
#     -p 5849:8080 \
#     -p 50000:50000 \
#     -e JAVA_OPT="${JUBAKY_JAVA_OPT}" \
#     -e DB_URL="${JUBAKY_DB_URL}" \
#     -e DB_USERNAME="${JUBAKY_DB_USERNAME}" \
#     -e DB_PASSWORD="${JUBAKY_DB_PASSWORD}" \
#     -e TOKEN_SECRET="${JUBAKY_TOKEN_SECRET}" \
#     -e JENKINS_URL="${JUBAKY_JENKINS_URL}" \
#     -e JENKINS_USERNAME="${JUBAKY_JENKINS_USERNAME}" \
#     -e JENKINS_TOKEN="${JUBAKY_JENKINS_TOKEN}" \
#     -e KUBERNETES_KUBECONFIG_NAME=${JUBAKY_KUBERNETES_KUBECONFIG_NAME} \
#     -v ${JUBAKY_JENKINS_CONFIG_PATH}:/var/jenkins_home \
#     -v ${JUBAKY_KUBERNETES_KUBECONFIG_PATH}:/var/kubernetes_home \
#     -v /var/run/docker.sock:/var/run/docker.sock \
#     -v /usr/bin/docker:/usr/bin/docker \
#     -u root \
#     --privileged \
#     --name jubaky-api-server \
#     --restart always \
#     jbkcose/jubaky-api-server:dev
