val swaggerSpringDocVersion = "2.2.0"

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.openai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://jitpack.io") }

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.ai:spring-ai-weaviate-store-spring-boot-starter:1.0.0-SNAPSHOT")
    implementation("org.springframework.cloud:spring-cloud-function-context:4.0.5")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-SNAPSHOT")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:$swaggerSpringDocVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerSpringDocVersion")
    implementation("org.springdoc:springdoc-openapi-starter-common:$swaggerSpringDocVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.data:spring-data-redis")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("it.ozimov:embedded-redis:0.7.2")
    implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.5.3")
    implementation("com.github.shin285:KOMORAN:3.3.9")
    implementation("org.apache.opennlp:opennlp-tools:2.0.0")
    implementation("org.apache.opennlp:opennlp-uima:2.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito:mockito-inline:4.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
