description = "selcukes-junit"

val agent: Configuration by configurations.creating

dependencies {
    api(projects.selcukesTestng)
    api(rootProject.libs.bundles.junit)
    testImplementation(rootProject.libs.cucumberJava)
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
