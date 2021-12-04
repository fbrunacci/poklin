import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}
group = "fbrunacci.poklin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.h2database:h2:1.3.168")
    implementation("com.google.inject:guice:3.0")
    implementation("dev.misfitlabs.kotlinguice4:kotlin-guice:1.5.0")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation(kotlin("test-junit"))
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}