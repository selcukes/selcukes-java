description = "selcukes-extent-reports"
val agent: Configuration by configurations.creating
dependencies {
    compileOnly("org.seleniumhq.selenium:selenium-java:3.141.59")
    compileOnly("org.testng:testng:7.4.0")
    compileOnly("io.cucumber:cucumber-java:6.11.0")
    compileOnly("io.cucumber:cucumber-testng:6.11.0")
    compileOnly("io.cucumber:cucumber-picocontainer:6.11.0")
    compileOnly("com.aventstack:extentreports:5.0.8")
    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly(project(":webdriver-binaries"))
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
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}
