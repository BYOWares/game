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

import model.Version
import model.Versions
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @since XXX
 */
abstract class SanitizeVersionsFileTask : DefaultTask() {

    init {
        group = BasePlugin.BUILD_GROUP
        description =
            "Sanitize (add missing, remove non existing) the modules list in the versions file, with the ${Version.UNKNOWN} version."
    }

    @get:InputFile
    abstract var versionsFile: Provider<File>

    @TaskAction
    fun checkFile() {
        val versions = Versions.parse(versionsFile.get())
        val modules = project.childProjects.keys.toSet()
        versions.sanitizeModules(modules)
    }

    companion object {
        const val SANITIZE_VERSIONS_FILE_TASK_NAME = "sanitizeVersionsFile"
    }
}