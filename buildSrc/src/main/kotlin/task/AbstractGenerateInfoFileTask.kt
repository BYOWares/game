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

package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class AbstractGenerateInfoFileTask : DefaultTask() {

    @Input
    val pkg = "fr.byowares.game." + project.name.replace('-', '.') + ".info"

    @Input
    val infoFilePath = "src-generated/main/java/" + pkg.replace('.', '/')

    @get:Input
    abstract var version: String

    @get:InputFile
    abstract var copyrightFile: File

    @get:OutputFile
    abstract val outputFile: File

    abstract fun generateBody(moduleName: String): String

    fun resolveOutputFile(fileName: String): File {
        return project.projectDir.resolve("$infoFilePath/$fileName.java")
    }

    @TaskAction
    fun generateInfoFile() {
        val moduleName = project.name.replace('-', '.')
        val indent = "            "
        val copyright = Copyright.extractCopyright(copyrightFile).asJavaDoc(indent)
        val s = System.lineSeparator()
        // Starting by an empty new line to have a clean indentation.
        val body = (s + generateBody(moduleName)).lines().joinToString(s) { l -> if (l.isEmpty()) l else "$indent$l" }
        outputFile.parentFile.mkdirs()
        outputFile.createNewFile()
        outputFile.writeText(
            """
            $copyright
            $body
            """.trimIndent()
        )
    }
}
