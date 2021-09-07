description = "selcukes-junit"

val agent: Configuration by configurations.creating

dependencies {
    implementation(project(":selcukes-testng"))
    testImplementation("org.junit.platform:junit-platform-suite:1.8.0-RC1")
    testImplementation("io.cucumber:cucumber-java:6.11.0")
    compileOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    compileOnly("io.cucumber:cucumber-junit-platform-engine:6.11.0")
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.junit"
        ))
    }
}
tasks.test {
    useTestNG()
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}
