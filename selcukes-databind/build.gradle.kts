description = "selcukes-databind"
val agent: Configuration by configurations.creating
dependencies {
    api(libs.jacksonYml)
    testImplementation(libs.testng)
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
