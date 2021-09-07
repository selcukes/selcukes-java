description = "webdriver-binaries"
val agent: Configuration by configurations.creating
dependencies {
    api(project(":selcukes-commons"))
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.jsoup:jsoup:1.14.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    testImplementation("org.testng:testng:7.4.0")
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