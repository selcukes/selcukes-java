description = "selcukes-core"
val agent: Configuration by configurations.creating
dependencies {
    implementation(project(":webdriver-binaries"))
    implementation("org.testng:testng:7.4.0")
    compileOnly("io.appium:java-client:7.5.1")
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.apache.commons:commons-lang3:3.12.0")
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
