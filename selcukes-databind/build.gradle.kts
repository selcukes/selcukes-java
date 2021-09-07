description = "selcukes-databind"
val agent: Configuration by configurations.creating
dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.5")
    testImplementation("org.testng:testng:7.4.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
}
tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.databind"
        ))
    }
}
tasks.test {
    useTestNG()
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}
