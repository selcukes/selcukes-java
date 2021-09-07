description = "Selcukes common"
val agent: Configuration by configurations.creating
dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation(project(":selcukes-databind"))
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
    testImplementation("org.testng:testng:7.4.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
}
tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.commons"
        ))
    }
}
tasks.test {
    useTestNG()
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}

