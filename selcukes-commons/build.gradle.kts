description = "Selcukes commons"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesDatabind)
    api(libs.commonsIo)
    api(libs.httpClient)
    testImplementation(libs.testng)
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

