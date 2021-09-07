description = "selcukes-server"

val agent: Configuration by configurations.creating
dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.seleniumhq.selenium:selenium-server:3.141.59")
    api(project(":selcukes-reports"))
}


tasks.jar {
    manifest {
        attributes(mapOf(
            "Automatic-Module-Name" to "io.github.selcukes.server"
        ))
    }
}
tasks.test {
    useTestNG()
    doFirst {
        jvmArgs("-javaagent:${agent.singleFile}")
    }
}