val linkHomepage by extra("https://github.com/selcukes")
val linkCi by extra("https://ci.qameta.in/job/selcukes-java_deploy/")
val linkScmUrl by extra("https://github.com/selcukes/selcukes-java")
val linkScmConnection by extra("scm:git:git://github.com/selcukes/selcukes-java.git")
val linkScmDevConnection by extra("scm:git:ssh://git@github.com:selcukes/selcukes-java.git")
val libs = subprojects.filterNot { it.name in "selcukes-bom" }

tasks.withType(Wrapper::class) {
    gradleVersion = "7.1.1"
}

plugins {
    java
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin")
    id("org.sonarqube")

}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

configure(listOf(rootProject)) {
    description = "Selcukes Java"
    group = "io.github.selcukes"
}
sonarqube {
    properties {
        property("sonar.projectKey", "selcukes_selcukes-java")
        property("sonar.organization", "selcukes")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
nexusPublishing {
    repositories {
        sonatype()
    }
}
configure(subprojects) {
    group = "io.github.selcukes"
    version = version

    apply(plugin = "signing")
    apply(plugin = "maven-publish")

    publishing {
        publications {
            create<MavenPublication>("maven") {
                suppressAllPomMetadataWarnings()
                versionMapping {
                    allVariants {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set(project.name)
                    description.set("Module ${project.name} of Selcukes Framework.")
                    url.set("https://github.com/selcukes/selcukes-java")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("rprudhvi")
                            name.set("Ramesh Babu Prudhvi")
                        }

                    }
                    scm {
                        developerConnection.set("scm:git:git://github.com/selcukes/selcukes-java")
                        connection.set("scm:git:git://github.com/selcukes/selcukes-java")
                        url.set("https://github.com/selcukes/selcukes-java")
                    }
                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("https://github.com/selcukes/selcukes-java/issues")
                    }
                }
            }
        }
    }

    signing {
        sign(publishing.publications["maven"])
    }

    tasks.withType<Sign>().configureEach {
        onlyIf { !project.version.toString().endsWith("-SNAPSHOT") }
    }

    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
}
configure(libs) {
    val project = this
    apply(plugin = "java")
    apply(plugin = "java-library")
    dependencies {
        implementation("commons-io:commons-io:2.11.0")
        implementation(project(":selcukes-databind"))
        implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
        testImplementation("org.testng:testng:7.4.0")
        compileOnly("org.projectlombok:lombok:1.18.20")
    }

    val internal by configurations.creating {
        isVisible = false
        isCanBeConsumed = false
        isCanBeResolved = false
    }
    configurations.compileClasspath.get().extendsFrom(internal)
    configurations.runtimeClasspath.get().extendsFrom(internal)
    configurations.testCompileClasspath.get().extendsFrom(internal)
    configurations.testRuntimeClasspath.get().extendsFrom(internal)

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }

        compileTestJava {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-parameters")
        }

        jar {
            manifest {
                attributes(
                    mapOf(
                        "Specification-Title" to project.name,
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to project.version
                    )
                )
            }
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks.withType(Javadoc::class) {
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    publishing.publications.named<MavenPublication>("maven") {
        pom {
            from(components["java"])
        }
    }
}