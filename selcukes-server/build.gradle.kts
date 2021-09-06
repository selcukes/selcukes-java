plugins {
    id("io.github.selcukes.java-conventions")
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.seleniumhq.selenium:selenium-server:3.141.59")
    implementation(project(":selcukes-reports"))
}

description = "selcukes-server"
