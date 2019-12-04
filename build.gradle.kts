plugins {
    kotlin("jvm") version "1.3.60"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.3.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }


    named<Test>("test") {
        useJUnitPlatform()
    }


}