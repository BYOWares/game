/*
 * Copyright BYOWares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import task.GenerateJavaInfoFileTask
import task.GeneratePackageInfoFileTask

plugins {
    // Apply the java Plugin to add support for Java.
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.12.0")
    }

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            srcDir("src-generated/main/java")
        }
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

val groupIdProvider = project.providers.provider { "fr.byowares" }
val baseProjectNameProvider = project.providers.provider { "game" }
val versionFileProvider = project.providers.provider { projectDir.parentFile.resolve("versions.yml") }
val copyrightFileProvider = project.providers.provider { projectDir.parentFile.resolve(".idea/copyright/BYOWares.xml") }

val genJavaInfoFile = "generateJavaInfoFile"
val genPkgInfoFile = "generatePackageInfoFile"

val rs = project.properties["BUILD_SUFFIX"]?.toString() ?: if (project.hasProperty("BUILD_RELEASE")) "" else "SNAPSHOT"
val suffix = if (rs.isEmpty() || rs.startsWith("-")) rs else "-$rs"

tasks.register<GeneratePackageInfoFileTask>(genPkgInfoFile) {
    groupId = groupIdProvider
    baseProjectName = baseProjectNameProvider
    versionsFile = versionFileProvider
    copyrightFile = copyrightFileProvider
}

tasks.register<GenerateJavaInfoFileTask>(genJavaInfoFile) {
    groupId = groupIdProvider
    baseProjectName = baseProjectNameProvider
    versionsFile = versionFileProvider
    copyrightFile = copyrightFileProvider
    versionSuffix = project.providers.provider { suffix }
}

tasks.named("compileJava") { dependsOn(genJavaInfoFile) }
tasks.named("generateJavaInfoFile") { dependsOn(genPkgInfoFile) }
