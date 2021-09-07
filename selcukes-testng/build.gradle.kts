description = "selcukes-testng"
val agent: Configuration by configurations.creating
dependencies {
    implementation(project(":selcukes-commons"))
    testImplementation("io.cucumber:cucumber-java:6.11.0")
    compileOnly("io.cucumber:cucumber-testng:6.11.0")
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
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}
