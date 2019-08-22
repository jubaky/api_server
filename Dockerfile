FROM jbkcose/jubaky-jenkins:lts
MAINTAINER jubaky "jbkcose@gmail.com"

USER root

# Environments of jubaky
ENV JAVA_OPT java_opt

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

ARG TOKEN_SECRET

ARG JENKINS_URL
ARG JENKINS_USERNAME
ARG JENKINS_TOKEN

ARG KUBERNETES_KUBECONFIG_NAME

# Copy all files
COPY . /api_server
WORKDIR /api_server

# Set environments to the property file
RUN ./setProperty.sh db.jubaky.url ${DB_URL} common.properties && \
    ./setProperty.sh db.jubaky.username ${DB_USERNAME} common.properties && \
    ./setProperty.sh db.jubaky.password ${DB_PASSWORD} common.properties && \
    ./setProperty.sh token.jubaky.secret ${TOKEN_SECRET} common.properties && \
    ./setProperty.sh jenkins.url ${JENKINS_URL} common.properties && \
    ./setProperty.sh jenkins.username ${JENKINS_USERNAME} common.properties && \
    ./setProperty.sh jenkins.token ${JENKINS_TOKEN} common.properties && \
    ./setProperty.sh kubernetes.kubeconfig.path /var/kubernetes_home/${KUBERNETES_KUBECONFIG_NAME} common.properties

# Build gradle
RUN chmod +x ./gradlew
RUN ./gradlew distTar
WORKDIR ./build/distributions
RUN tar -xvf $(ls jubaky-*.tar | tail -n 1) --strip-components 1

WORKDIR ./bin

# Run application
CMD ["./jubaky"]
