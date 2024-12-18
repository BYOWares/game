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
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @since XXX
 */
abstract class CopyLog4JFileTask : DefaultTask() {
    init {
        group = BasePlugin.BUILD_GROUP
        description = "Copy the YAML Log4J2 configuration from plugin to project."
    }

    @get:InputFile
    abstract var log4J2ConfigFile: Provider<File>

    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty().convention(
        project.layout.projectDirectory.dir("src").dir("main").dir("resources")
    )

    @TaskAction
    fun copyFile() {
        val file = log4J2ConfigFile.get()
        val updatedContent = file.readText().replace("PROJECT_NAME", project.name)
        outputDirectory.get().asFile.resolve(file.name).writeText(updatedContent)
    }
}
