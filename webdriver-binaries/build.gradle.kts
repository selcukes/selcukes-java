description = "webdriver-binaries"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    implementation(rootProject.libs.commonsCompress)
    implementation(rootProject.libs.jsoup)
    testImplementation(rootProject.libs.selenium)
    testImplementation(rootProject.libs.testng)
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.wdb"
        ))
    }
}
tasks.test {
    useTestNG()
}