// Lib Version
version = "release-0.1.0"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.31"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to rootProject.name,
                         "Implementation-Version" to project.version))
    }
    archiveBaseName.set(rootProject.name)
}

repositories {
    mavenCentral()
    jcenter()
}

configurations {
    val integrationTestCompile by configurations.creating {
        extendsFrom(configurations["implementation"])
    }

    val integrationTestRuntime by configurations.creating {
        extendsFrom(configurations["integrationTestCompile"])
    }

    dependencies {
        integrationTestCompile("org.junit.jupiter:junit-jupiter-engine:5.5.2")
        integrationTestRuntime("org.junit.jupiter:junit-jupiter-api:5.5.2")
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:30.1.1-jre")

     // Use to Test
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // HTTP resources
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // GSON JSON resources
    implementation ("com.google.code.gson:gson:2.8.7")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
}

sourceSets {
    val integrationTest by creating {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output

        java {
            srcDir("src/integration-test/kotlin")
        }

        resources {
            srcDir("src/integration-test/resources")
        }
    }
}

task<Test>("integrationTest") {
    description = "Task to run integration tests"
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()
    shouldRunAfter("test")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
}
