FROM openjdk:8-jdk-alpine

ENV SERVICE_ENV=dev
ENV SERVICE_PORT=8080

EXPOSE ${SERVICE_PORT}

ENTRYPOINT ["/jubaky/bin/jubaky"]
CMD ["bash"]

RUN apk --update --no-cache add bash curl unzip

COPY ./build/install/jubaky/ /jubaky/
COPY ./kubeconfig-445.yaml /jubaky/kubeconfig.yaml
COPY ./job_config_default.xml /jubaky/job_config_default.xml

RUN chmod 700 -R /jubaky/
