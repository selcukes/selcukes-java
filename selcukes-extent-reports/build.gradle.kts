description = "selcukes-extent-reports"
val agent: Configuration by configurations.creating
dependencies {
    compileOnly(rootProject.libs.selenium)
    compileOnly(rootProject.libs.testng)
    implementation(rootProject.libs.bundles.cucumber)
    implementation(rootProject.libs.extentReports)
    implementation(projects.webdriverBinaries)
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.extent.reports"
        ))
    }
}
tasks.test {
    useTestNG()
}
