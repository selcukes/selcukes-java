description = "selcukes-testng"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    testImplementation(libs.cucumberJava)
    api(libs.cucumberTestNG)
    compileOnly(libs.testng)
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.testng"
        ))
    }
}
tasks.test {
    useTestNG()
}
