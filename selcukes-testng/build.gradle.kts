description = "selcukes-testng"
val agent: Configuration by configurations.creating
dependencies {
    api(project(":selcukes-commons"))
    testImplementation("io.cucumber:cucumber-java:6.11.0")
    api("io.cucumber:cucumber-testng:6.11.0")
    compileOnly("org.testng:testng:7.4.0")
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.testng"
        ))
    }
}
tasks.test {
    useTestNG()
}
