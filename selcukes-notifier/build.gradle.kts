description = "selcukes-notifier"

dependencies {
    implementation(projects.selcukesCommons)
    testImplementation(rootProject.libs.testng)
}

tasks.test {
    useTestNG()
}
