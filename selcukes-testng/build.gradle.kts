plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation(project(":selcukes-commons"))
    testImplementation("io.cucumber:cucumber-java:6.11.0")
    compileOnly("io.cucumber:cucumber-testng:6.11.0")
    compileOnly("org.testng:testng:7.4.0")
}

description = "selcukes-testng"

tasks.test {
    useTestNG()
}
