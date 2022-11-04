import com.github.javaparser.ParserConfiguration.LanguageLevel

plugins {
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.12.0"
}

group = "top.berthua"
version = "1.2.9-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
dependencies {
    implementation("com.alibaba:fastjson:2.0.17")
    implementation("com.baidubce:api-explorer-sdk:1.0.4.1")
}
