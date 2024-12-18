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

import extension.BYOWaresExtension
import task.CopyLog4JFileTask
import task.GenerateJavaInfoFileTask
import task.GeneratePackageInfoFileTask
import task.SanitizeVersionsFileTask
import task.UpdateSinceTagTask

plugins {
    java
}

repositories {
    mavenCentral()
}

configurations.configureEach { isTransitive = false }

// Java toolchain to ease working on different environments.
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

val genJavaInfoFile = "generateJavaInfoFile"
val genPkgInfoFile = "generatePackageInfoFile"
val copyLog4J2Conf = "copyLog4J2Conf"
val byoExt = rootProject.extensions.getByType(BYOWaresExtension::class.java)

tasks.register<GeneratePackageInfoFileTask>(genPkgInfoFile) {
    groupId = byoExt.groupId
    baseProjectName = byoExt.baseProjectName
    versionsFile = byoExt.versionsFile.asFile
    copyrightFile = byoExt.copyrightFile.asFile
}

tasks.register<GenerateJavaInfoFileTask>(genJavaInfoFile) {
    groupId = byoExt.groupId
    baseProjectName = byoExt.baseProjectName
    versionsFile = byoExt.versionsFile.asFile
    copyrightFile = byoExt.copyrightFile.asFile
    versionSuffix = byoExt.versionSuffix
}

tasks.register<UpdateSinceTagTask>(UpdateSinceTagTask.UPDATE_SINCE_TAG_TASK_NAME) {
    versionsFile = byoExt.versionsFile.asFile
    updateVersionsFile.set(false)
}

tasks.register<CopyLog4JFileTask>(copyLog4J2Conf) {
    log4J2ConfigFile = byoExt.log4J2ConfigFile.asFile
}

tasks.named(JavaPlugin.COMPILE_JAVA_TASK_NAME) { dependsOn(genJavaInfoFile) }
tasks.named(genJavaInfoFile) { dependsOn(genPkgInfoFile) }
tasks.named(genJavaInfoFile) { dependsOn(rootProject.tasks.named(SanitizeVersionsFileTask.SANITIZE_VERSIONS_FILE_TASK_NAME)) }
tasks.named(JavaPlugin.CLASSES_TASK_NAME) { dependsOn(copyLog4J2Conf) }
