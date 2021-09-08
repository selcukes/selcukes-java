description = "selcukes-extent-reports"
val agent: Configuration by configurations.creating
dependencies {
    compileOnly(libs.selenium)
    compileOnly(libs.testng)
    implementation(libs.bundles.cucumber)
    implementation(libs.extentReports)
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
