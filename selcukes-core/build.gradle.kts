plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation(project(":webdriver-binaries"))
    implementation("org.testng:testng:7.4.0")
    compileOnly("io.appium:java-client:7.5.1")
    compileOnly("org.seleniumhq.selenium:selenium-java:3.141.59")
}

description = "selcukes-core"

tasks.test {
    useTestNG()
}
