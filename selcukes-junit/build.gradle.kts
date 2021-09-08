description = "selcukes-junit"

val agent: Configuration by configurations.creating

dependencies {
    api(projects.selcukesTestng)
    api("org.junit.platform:junit-platform-suite:1.8.0-RC1")
    testImplementation(rootProject.libs.cucumberJava)
    api("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    api("io.cucumber:cucumber-junit-platform-engine:6.11.0")
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.junit"
        ))
    }
}
tasks.test {
    useTestNG()
}
