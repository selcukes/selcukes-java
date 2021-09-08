enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "selcukes-parent"
include(":selcukes-bom")
include(":selcukes-databind")
include(":selcukes-commons")
include(":webdriver-binaries")
include(":selcukes-core")
include(":selcukes-testng")
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
        id("org.sonarqube") version "3.3"
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
