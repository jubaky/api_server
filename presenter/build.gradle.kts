dependencies {
    api(project(":base"))

    implementation(project(":jubaky-domain"))

    implementation("io.ktor:ktor-server-core:1.2.2")
    implementation("io.ktor:ktor-server-netty:1.2.2")
    implementation("io.ktor:ktor-jackson:1.2.2")
    implementation("io.ktor:ktor-auth:1.2.2")
    implementation("io.ktor:ktor-auth-jwt:1.2.2")
}