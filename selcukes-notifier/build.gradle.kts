plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation(project(":selcukes-commons"))
    testImplementation("org.testng:testng:7.4.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
}

description = "selcukes-notifier"

tasks.test {
    useTestNG()
}
