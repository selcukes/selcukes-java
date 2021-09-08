description = "selcukes-reports"

val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    api(projects.selcukesNotifier)
    implementation(rootProject.libs.cucumberReporting)
    implementation(rootProject.libs.recorder)
    testImplementation(projects.webdriverBinaries)
    testImplementation(rootProject.libs.testng)
    api(rootProject.libs.bundles.cucumber)
    api(rootProject.libs.selenium)
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