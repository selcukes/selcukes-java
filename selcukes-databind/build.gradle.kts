description = "selcukes-databind"
val agent: Configuration by configurations.creating
dependencies {
    api(rootProject.libs.jacksonYml)
    testImplementation(rootProject.libs.testng)
}
tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.databind"
        ))
    }
}
tasks.test {
    useTestNG()
}
