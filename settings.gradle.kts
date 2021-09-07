rootProject.name = "selcukes-parent"
include(":selcukes-bom")
include(":selcukes-databind")
include(":selcukes-commons")
include(":selcukes-core")
include(":selcukes-testng")
include(":webdriver-binaries")
include(":selcukes-notifier")
include(":selcukes-reports")
include(":selcukes-extent-reports")
include(":selcukes-server")
include(":selcukes-junit")
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.gradle.enterprise") version "3.6.2"
        id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    }
}
plugins {
    id("com.gradle.enterprise")
}

val isCiServer = System.getenv().containsKey("CI")

if (isCiServer) {
    gradleEnterprise {
        buildScan {
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
            tag("CI")
        }
    }
}
