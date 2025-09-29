plugins {
    kotlin("jvm") version "2.2.0"
    id("application")
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

// 실행 가능한 Fat JAR 생성 설정
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().files.filter { it.isDirectory || it.name.endsWith("jar") }.map { if (it.isDirectory) it else zipTree(it) }
    })
}