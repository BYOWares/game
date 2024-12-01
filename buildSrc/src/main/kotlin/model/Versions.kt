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
package model

import org.gradle.api.GradleException
import org.gradle.internal.extensions.stdlib.uncheckedCast
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files

class Versions private constructor(
    private val file: File,
    private val versionToPublish: Version,
    private val modulesFirstVersion: MutableMap<String, Version>
) {
    fun getVersionToPublish(): Version {
        return versionToPublish
    }

    fun getModuleFirstVersion(module: String): Version? {
        return modulesFirstVersion[module]
    }

    fun bumpNextMajorVersionAndDumpFile() {
        versionToPublish.bumpNextMajor()
        dumpFile()
    }

    fun bumpNextMinorVersionAndDumpFile() {
        versionToPublish.bumpNextMinor()
        dumpFile()
    }

    fun bumpNextPatchVersionAndDumpFile() {
        versionToPublish.bumpNextPatch()
        dumpFile()
    }

    fun updateUnknownVersionToNextVersionAndDumpFile() {
        for (key in modulesFirstVersion.keys) {
            if (modulesFirstVersion[key] == Version.UNKNOWN) modulesFirstVersion[key] = versionToPublish
        }
        dumpFile()
    }

    fun dumpFile() {
        val data = mutableMapOf<String, Any>()
        data[VERSION_TO_PUBLISH_KEY] = versionToPublish.toString()
        data[MODULES_FIRST_VERSION_KEY] = modulesFirstVersion.mapValues { (_, v) -> v.toString() }.toSortedMap()
        Yaml().dump(data, FileWriter(file))
    }

    companion object {
        private const val VERSION_TO_PUBLISH_KEY = "versionToPublish"
        private const val MODULES_FIRST_VERSION_KEY = "modulesFirstVersion"

        fun parse(versionsFile: File): Versions {
            try {
                val versionsPath = versionsFile.toPath()
                if (!Files.exists(versionsPath) || !Files.isRegularFile(versionsPath)) {
                    return Versions(versionsFile, Version.UNKNOWN, HashMap())
                }

                val map = Yaml().load<Map<String, Any>>(FileInputStream(versionsFile))
                val versionToPublish = map.getOrDefault(VERSION_TO_PUBLISH_KEY, Version.UNKNOWN).toString() //

                val modulesFirstVersion = mutableMapOf<String, Version>()
                map.getOrDefault(MODULES_FIRST_VERSION_KEY, ArrayList<Map<String, String>>()) //
                    .uncheckedCast<List<Map<String, String>>>() //
                    .forEach { m -> m.forEach { (k, v) -> modulesFirstVersion[k] = Version.parse(v) } }

                return Versions(versionsFile, Version.parse(versionToPublish), modulesFirstVersion)
            } catch (e: IOException) {
                throw GradleException("Cannot get Copyright content", e)
            }
        }
    }
}
