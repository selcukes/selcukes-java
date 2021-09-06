plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.5")
    testImplementation("org.testng:testng:7.4.0")
    compileOnly("org.projectlombok:lombok:1.18.20")
}

description = "selcukes-databind"

tasks.test {
    useTestNG()
}
