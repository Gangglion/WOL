plugins {
    kotlin("jvm") version "2.2.0"
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