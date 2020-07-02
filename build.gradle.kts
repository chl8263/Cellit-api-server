import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    kotlin("jvm") version "1.3.71"
    kotlin("kapt") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    kotlin("plugin.jpa") version "1.3.71"
    //"1.3.61"
}

group = "me.ewan"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation(group = "org.springframework.security.oauth.boot", name = "spring-security-oauth2-autoconfigure", version = "2.2.5.RELEASE")

    // for query dsl
    api("com.querydsl:querydsl-jpa:4.2.2")
    kapt("com.querydsl:querydsl-apt:4.2.2:jpa")
    kapt("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")

    // https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation(group = "com.auth0", name = "java-jwt", version = "3.10.3")

    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.5")
    implementation(group = "org.modelmapper", name = "modelmapper", version = "2.3.2")

    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation("org.jetbrains.kotlin:kotlin-noarg")
    implementation("org.jetbrains.kotlin:kotlin-allopen")
    implementation("au.com.console:kassava:2.1.0-rc.1")

    implementation("com.h2database:h2")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

noArg {
    annotation("javax.persistence.Entity")
}

allOpen {
    annotation("javax.persistence.Entity")
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

//tasks.test {
//    outputs.dir(snippetsDir)
//}
//
//tasks.asciidoctor {
//    inputs.dir(snippetsDir)
//    dependsOn(test)
//}
