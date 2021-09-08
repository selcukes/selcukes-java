description = "selcukes-testng"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    testImplementation(rootProject.libs.cucumberJava)
    api(rootProject.libs.cucumberTestNG)
    compileOnly(rootProject.libs.testng)
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
