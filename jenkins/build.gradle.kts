dependencies {
    api(project(":base"))

    implementation(project(":jubaky-domain"))

    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.6.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.offbytwo.jenkins:jenkins-client:0.3.8")
}