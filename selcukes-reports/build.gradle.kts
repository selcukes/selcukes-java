description = "selcukes-reports"

val agent: Configuration by configurations.creating
dependencies {
    implementation(project(":selcukes-commons"))
    implementation(project(":selcukes-notifier"))
    implementation("net.masterthought:cucumber-reporting:5.6.0")
    implementation("com.github.stephenc.monte:monte-screen-recorder:0.7.7.0")
    testImplementation(project(":webdriver-binaries"))
    compileOnly("org.testng:testng:7.4.0")
    compileOnly("io.cucumber:cucumber-java:6.11.0")
    compileOnly("io.cucumber:cucumber-testng:6.11.0")
    compileOnly("io.cucumber:cucumber-picocontainer:6.11.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
    compileOnly("org.seleniumhq.selenium:selenium-java:3.141.59")
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
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}