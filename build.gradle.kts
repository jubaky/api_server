import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
}

group = "org.jaram"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply {
        plugin("kotlin")
    }

    dependencies {
        val jacksonVersion = "2.9.9"

        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")

        implementation("org.slf4j:slf4j-api:1.7.26")
        implementation("org.slf4j:jul-to-slf4j:1.7.26")
        implementation("ch.qos.logback:logback-classic:1.2.3")

        implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")

        implementation("joda-time:joda-time:2.10.3")

        implementation("com.google.guava:guava:28.0-jre")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

val childJars by configurations.creating

dependencies {
    subprojects.forEach {
        childJars(project(it.path))
    }
}

tasks.withType<Jar> {
    archiveFileName.set("jubaky.jar")
    manifest {
        attributes["Main-Class"] = "org.jaram.jubaky.MainKt"
    }
    dependsOn(childJars)
    from(childJars.map { if (it.isDirectory) it else zipTree(it) })
}