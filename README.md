# Jubkay - api_server

Jenkins와 Kubernetes API를 이용한 Jubaky Back End Repository

## 프로그램 설치 및 실행 가이드
#### 1. Clone Jubaky repository
```
git clone https://github.com/jubaky/api_server
```

#### 2. Set properties. - api_server/assemble/src/main/resources/*.properties
```
### Kubernetes
kubernetes.kubeconfig.path=/jubaky/kubeconfig.yaml

### DB
db.jubaky.driver=com.mysql.cj.jdbc.Driver
db.jubaky.url=
db.jubaky.username=
db.jubaky.password=

### JWT
token.jubaky.jwtIssuer=jubaky.org
token.jubaky.secret=
token.jubaky.jwtAudience=jubaky-audience
token.jubaky.validityInMs=36000000

### Jenkins
jenkins.url=
jenkins.username=
jenkins.token=
```

#### 3. Move kubeconfig file to root directory.

#### 4. Modify Docker file.
```
FROM openjdk:8-jdk-alpine

ENV SERVICE_ENV=dev
ENV SERVICE_PORT=8080

EXPOSE ${SERVICE_PORT}

ENTRYPOINT ["/jubaky/bin/jubaky"]
CMD ["bash"]

RUN apk --update --no-cache add bash curl unzip

COPY ./build/install/jubaky/ /jubaky/
COPY ./{your_k8s_config_file} /jubaky/kubeconfig.yaml
COPY ./job_config_default.xml /jubaky/job_config_default.xml

RUN chmod 700 -R /jubaky/
```

#### 5. Build Docker.
```
./gradlew install
sudo docker build {your_jubaky_docker_image_name} .
```

#### 6. Run Docker.
```
sudo docker run \
  -d \
  -p 5850:8080 
  {your_jubaky_docker_image_name}
```
