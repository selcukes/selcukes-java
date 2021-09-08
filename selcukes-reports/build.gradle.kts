description = "selcukes-reports"

val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    api(projects.selcukesNotifier)
    implementation(libs.cucumberReporting)
    implementation(libs.recorder)
    testImplementation(projects.webdriverBinaries)
    testImplementation(libs.testng)
    api(libs.bundles.cucumber)
    api(libs.selenium)
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.reports"
        ))
    }
}
tasks.test {
    useTestNG()
}