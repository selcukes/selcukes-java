description = "webdriver-binaries"
val agent: Configuration by configurations.creating
dependencies {
    api(projects.selcukesCommons)
    implementation(libs.commonsCompress)
    implementation(libs.jsoup)
    testImplementation(libs.selenium)
    testImplementation(libs.testng)
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