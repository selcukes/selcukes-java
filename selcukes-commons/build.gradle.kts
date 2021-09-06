plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation(project(":selcukes-databind"))
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
    testImplementation("org.testng:testng:7.4.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
}

description = "selcukes-commons"

tasks.test {
    useTestNG()
}
