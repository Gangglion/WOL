import org.gradle.internal.impldep.junit.runner.Version.id

plugins {
    kotlin("jvm") version "2.2.0"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.glion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // API 호출을 위한 OkHttp 라이브러리
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    // main 함수가 있는 진입점 클래스 지정
    mainClass.set("com.glion.MainKt")
}
sourceSets["main"].resources {
    srcDirs(project.rootDir)
    include("local.properties")
}