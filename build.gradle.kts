import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
//    kotlin("jvm") version "1.5.31"
//    id("org.jetbrains.compose") version "1.0.0"
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
}


java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of("16")) // "8"
    }
}
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of("16")) // "8"
    }
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenCentral()
//    google()
//    maven("https://jcenter.bintray.com")
}
dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.h2database:h2:1.3.168")
    implementation("com.google.inject:guice:3.0")
    implementation("dev.misfitlabs.kotlinguice4:kotlin-guice:1.5.0")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")

    testImplementation(kotlin("test-junit"))
}

compose.desktop {
    application {
        mainClass = "poklin.compose.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Poklin"
        }
    }
}