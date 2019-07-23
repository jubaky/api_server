pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "jubaky"

include(
    "base",
    "assemble",
    "presenter",
    "domain",
    "db",
    "jenkins",
    "kubernetes"
)

findProject(":assemble")?.name = "jubaky-assemble"
findProject(":presenter")?.name = "jubaky-presenter"
findProject(":domain")?.name = "jubaky-domain"
findProject(":db")?.name = "jubaky-db"
findProject(":jenkins")?.name = "jubaky-jenkins-api"
findProject(":kubernetes")?.name = "jubaky-kubernetes-api"