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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.time.Instant

abstract class GenerateInfoFile : DefaultTask() {

    @Internal
    val timestamp = Instant.now().toEpochMilli()

    @Internal
    val pkg = "fr.byowares.game." + project.name.replace('-', '.') + ".info"

    @Internal
    val infoFilePath = "src-generated/main/java/" + pkg.replace('.', '/')

    @Internal
    val className = project.name.split("-").stream() //
        .map { p: String -> p[0].uppercaseChar() + p.substring(1) } //
        .toList().joinToString("") + "Info"

    @Input
    val revision = GitInfo.gitInfo(project.gradle.rootProject.rootDir).revision

    @get:Input
    abstract var version: String

    @OutputFile
    val infoFile = project.projectDir.resolve("$infoFilePath/$className.java")

    @TaskAction
    fun generateInfoFile() {
        val moduleName = project.name.replace('-', '.')
        infoFile.parentFile.mkdirs()
        infoFile.createNewFile()
        infoFile.writeText(
            """
            // Do not edit this generated file (see GenerateInfoFile task)
            package $pkg;

            /**
             * Information related to the <b>$moduleName</b> module
             */
            public final class $className {
                /** Version of the module */
                public static final String VERSION = "$version";
                /** Module build timestamp in milliseconds */
                public static final String BUILD_TIMESTAMP = "$timestamp";
                /** Current revision while building the module */
                public static final String REVISION = "$revision";
                /** A concatenation of all pieces of information */
                public static final String TO_STRING = "$className [version=" + VERSION + ", build-timestamp=" + BUILD_TIMESTAMP + ", revision=" + REVISION + "]";
                
                private $className() {
                    // Utility class
                }
            }
            """.trimIndent()
        )
    }
}
