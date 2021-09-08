description = "Selcukes commons"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesDatabind)
    api(rootProject.libs.commonsIo)
    api(rootProject.libs.httpClient)
    testImplementation(rootProject.libs.testng)
}
tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.commons"
        ))
    }
}
tasks.test {
    useTestNG()
}

