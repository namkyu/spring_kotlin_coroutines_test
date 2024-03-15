import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * 코틀린 DSL 을 사용한 build.gradle.kts 파일
 * 코틀린 DSL은 다른 DSL에 비해서 가독성이 높고, 유연한 코드를 작성할 수 있게 도와줌 (코틀린 문법 사용)
 *
 */

val imageName = "spring_kotlin_test"
val containerName = "spring_kotlin_test"

plugins {
    val kotlinVersion = "1.9.23"

    // 플러그인을 단독으로 적용하면 프로젝트에 아무런 변화가 없음
    // 대신 다른 플러그인이 적용되는 시점을 감지하고 그에 따라 반응함
    // ex) java 플러그인이 적용되면 실행 가능한 jar 빌드 작업이 자동으로 구성
    id("org.springframework.boot") version "3.2.5"

    // Spring Boot의 플러그인이 사용중인 Spring Boot 버전에서 Spring-boot-dependency bom을 자동으로 가져온다.
    // Maven과 같은 종속성 관리 기능을 제공하는 Gradle 플러그인, 의존성을 추가할 때 버전을 생략할 수 있습니다.
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    // Jib 플러그인은 Docker 이미지를 빌드하고 배포하는 데 사용
    id("com.google.cloud.tools.jib") version "3.4.2"

    // 코틀린 코드를 JVM 바이트 코드로 컴파일하는데 사용
    kotlin("jvm") version kotlinVersion

    // 스프링 프로젝트에서 코틀린 코드를 사용할 때 필요한 추가 기능을 제공
    kotlin("plugin.spring") version kotlinVersion

    // 컴파일 시에 애노테이션 프로세스를 실행할 수 있게 해줌
    kotlin("kapt") version kotlinVersion

    kotlin("plugin.serialization") version "1.4.31"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// repositories 블록은 프로젝트에서 사용할 라이브러리들을 검색하기 위한 저장소(repository)를 정의
repositories {
    mavenCentral()
}

dependencies {

    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux") // webclient 사용을 위해 추가
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // 코틀린 표준 라이브러리 포함 (자료구조, 함수, 클래스 등)
    implementation("org.jetbrains.kotlin:kotlin-reflect") // 코틀린 리플렉션 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // 코루틴 사용 시 필요
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3") // reactor + 코루틴 통합
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // data class 에 기본 생성자 없는 이슈 해결

    // third party library
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.modelmapper:modelmapper:3.2.0")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}

// 코틀린 컴파일러 옵션 구성
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17" // 코틀린 컴파일러가 생성하는 바이트 코드의 JVM 대상 버전을 지정
    }
}

// 프로젝트의 모든 테스트를 JUnit 플랫폼을 사용하여 실행
tasks.withType<Test> {
    useJUnitPlatform()
}

// docker 이미지 빌드
// Jib : Java Image Builder
// Dockerfile 도 필요없고 Docker Daemon 도 설치할 필요가 없다.
// jib : build한 후 생성된 jar파일로 Docker image를 만들어서 Repository에 Push
// jibBuildTar : build 한 후  생성된 jar파일로 Docker Image를 만들고 tar로 묶어 Disk에 저장. Docker Image를 파일로 전달할 때 사용
// jibDockerBuild : build 한 후 생성된 jar파일로 Docker Image를 만들고 현재 동작중인 Docker daemon으로 전달
tasks {
    jib {
        // docker 기본 이미지
        from {
            image = "openjdk:17-alpine"
        }
        // 빌드된 이미지 이름
        to {
            image = imageName // image에 url이 없으면 default로 docker hub에 push
            tags = setOf("latest")
        }
        container {
            format = ImageFormat.OCI
            creationTime = "USE_CURRENT_TIMESTAMP" // docker image 생성 시간을 현재 시간으로 설정
            mainClass = "com.example.ApplicationKt"
            ports = listOf("8080") // 컨테이너 오픈 포트
            volumes = listOf("/tmp")
        }
    }
}

// 코틀린 DSL 이용해서 새로운 태스크 등록
// Exec : 외부 프로세스를 실행하는 태스크
tasks.register<Exec>("runDockerContainer") {
    dependsOn("jibDockerBuild") // jibDockerBuild task 실행 후에 실행됨
    description = "Run Docker container using the built image"

    // Docker 이미지의 이름을 여기에 입력합니다.
    val dockerImageName = imageName
    val dockerContainerName = containerName
    commandLine("docker", "run", "-d", "-p", "8080:8080", "--name", dockerContainerName, dockerImageName)
}

tasks.register<Exec>("removeDockerContainer") {
    description = "Remove the running Docker container"
    commandLine("docker", "rm", "-f", containerName)
}