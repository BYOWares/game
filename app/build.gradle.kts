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

plugins {
    id("byogame.java-library")
    id("byogame.java-fx")
    id("byogame.java-application")
}

dependencies {
    implementation(project(":utils"))
    implementation(project(":utils-jfx"))
    implementation(project(":miq-core"))
    implementation(project(":miq-jfx"))

    implementation(libs.agrona)
    implementation(libs.bundles.atlantafx)
    implementation(libs.bundles.ikonli)
    implementation(libs.bundles.log4jImpl)
    implementation(libs.bundles.yamlImpl)
    runtimeOnly(libs.bundles.log4jRun)
    runtimeOnly(libs.bundles.mp3)

    testImplementation(libs.bundles.junitImpl)
    testRuntimeOnly(libs.bundles.junitRun)
}

application {
    mainClass = "fr.byowares.game.app.App"
    mainModule = "fr.byowares.game.app"
}
