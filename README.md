# BYO-Game
A collection of small games

## Environment
* IntelliJ IDEA 2024.3 (Community Edition
* Gradle 8.10.2
* Java 21

## Custom tasks
* ``./gradlew generateJavaInfoFile or ./gradlew gJIF``: generate java files with information about build
* ``./gradlew generatePkgInfoFile  or ./gradlew gPIF``: generate package info files for the previously generated java files
* ``./gradlew sanitizeVersionsFile or ./gradlew sVF``: sanitize the versions file (versions.yml)
* ``./gradlew bumpMajorVersion``: increase major version
* ``./gradlew bumpMinorVersion``: increase minor version
* ``./gradlew bumpPatchVersion``: increase patch version
* ``./gradlew updateSinceTag``: update unset `@since` tag in code
