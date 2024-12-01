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

/**
 * @since XXX
 */
class Version private constructor(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) : Comparable<Version> {

    override fun compareTo(other: Version): Int {
        if (this.major > other.major) return 1
        if (this.major < other.major) return -1

        if (this.minor > other.minor) return 1
        if (this.minor < other.minor) return -1

        if (this.patch > other.patch) return 1
        if (this.patch < other.patch) return -1

        return 0
    }

    fun bumpNextMajor(): Version {
        return Version(major + 1, 0, 0)
    }

    fun bumpNextMinor(): Version {
        return Version(major, minor + 1, 0)
    }

    fun bumpNextPatch(): Version {
        return Version(major, minor, patch + 1)
    }

    override fun toString(): String {
        return if (this == UNKNOWN) UNKNOWN_STR else "$major.$minor.$patch"
    }

    companion object {
        val UNKNOWN = Version(-1, -1, -1)
        const val UNKNOWN_STR = "XXX"

        fun parse(value: String): Version {
            if (value == UNKNOWN_STR) return UNKNOWN
            val first: Int = value.indexOf('.')
            val last: Int = value.lastIndexOf('.')
            val major = Integer.parseInt(value, 0, first, 10)
            val minor = Integer.parseInt(value, first + 1, last, 10)
            val patch = Integer.parseInt(value, last + 1, value.length, 10)
            return Version(major, minor, patch)
        }
    }
}
