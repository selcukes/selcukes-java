description = "selcukes-core"
val agent: Configuration by configurations.creating
dependencies {
    implementation(rootProject.projects.webdriverBinaries)
    implementation(rootProject.libs.testng)
    compileOnly(rootProject.libs.appium)
    implementation(rootProject.libs.selenium)
    implementation(rootProject.libs.commonsLang)
}
tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.core"
        ))
    }
}
tasks.test {
    useTestNG()
}
