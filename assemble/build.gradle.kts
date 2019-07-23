dependencies {
    api(project(":base"))

    implementation(project(":jubaky-presenter"))
    implementation(project(":jubaky-domain"))
    implementation(project(":jubaky-db"))
    implementation(project(":jubaky-jenkins-api"))
    implementation(project(":jubaky-kubernetes-api"))

    implementation("org.koin:koin-core:2.0.1")
    implementation("org.koin:koin-core-ext:2.0.1")

    implementation("com.zaxxer:HikariCP:3.3.1")
    implementation("mysql:mysql-connector-java:8.0.16")
}