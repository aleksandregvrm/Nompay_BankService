plugins {
  kotlin("jvm") version "2.2.0"
  kotlin("plugin.spring") version "2.2.0"
  id("org.springframework.boot") version "3.5.4"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("plugin.jpa") version "2.2.0"
}

group = "com.nompay"
version = "0.0.1-SNAPSHOT"
description = "Nompay banking rewriting with kotlin"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(23)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-graphql")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("com.auth0:java-jwt:4.4.0")
  implementation("org.mindrot:jbcrypt:0.4")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  runtimeOnly("com.mysql:mysql-connector-j")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
