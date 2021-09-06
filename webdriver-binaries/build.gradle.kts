plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation(project(":selcukes-commons"))
    implementation("org.apache.commons:commons-compress:1.21")
    implementation("org.jsoup:jsoup:1.14.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    testImplementation("org.testng:testng:7.4.0")
}

description = "webdriver-binaries"

tasks.test {
    useTestNG()
}
