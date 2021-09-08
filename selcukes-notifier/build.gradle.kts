description = "selcukes-notifier"

dependencies {
    implementation(projects.selcukesCommons)
    testImplementation(libs.testng)
}

tasks.test {
    useTestNG()
}
