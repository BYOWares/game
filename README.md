# BYO-Game

A collection of small games

## Environment

* IntelliJ IDEA 2024.3 (Community Edition
* Gradle 8.10.2
* Java 21

## Custom tasks

* ``./gradlew generateJavaInfoFile or ./gradlew gJIF``: generate java files with information about build
* ``./gradlew generatePkgInfoFile  or ./gradlew gPIF``: generate package info files for the previously generated java
  files
* ``./gradlew sanitizeVersionsFile or ./gradlew sVF``: sanitize the versions file (versions.yml)
* ``./gradlew bumpMajorVersion``: increase major version
* ``./gradlew bumpMinorVersion``: increase minor version
* ``./gradlew bumpPatchVersion``: increase patch version
* ``./gradlew updateSinceTag``: update unset `@since` tag in code
* ``./gradlew copyLog4J2Conf4Test``: copy the Log4J2 configuration file from the buildSrc to the test resources
* ``./gradlew copyLog4J2Conf``: copy the Log4J2 configuration file from the buildSrc to the resources

## GIT

In order to share git hooks, the **core.hooksPath** (git >= 2.9) is used on a versioned directory. Therefore, in
order to use them, you still need to configure the path locally:
``git config --local core.hooksPath .githooks/``.

## [Scripts](scripts)

All scripts shall be written in Bash.

### [sanitize_fxml.bash](scripts/sanitize_fxml.bash)

Make sure the **xmlns** declaring the javafx version is inline with the version used inside the project, thus preventing
warning logs while loading FXML. The Java Version is used, because we use it as well for the JavaFX version. See file:
[byogame.java-fx.gradle.kts](buildSrc/src/main/kotlin/byogame.java-fx.gradle.kts).

### [check_encoding.bash](scripts/check_encoding.bash)

List all files with encoding not supported. We do not put this script in pre-commit because it is slow on Windows.

### lib.*

Those scripts are libraries that other scripts can use. They have an _include_ mechanism that protect them from being
loaded multiple times (which can create issues with readonly variables). This mechanism is implemented by defining a
unique environment variable for each library. Here is the template to follow:

```bash
# Those two line make sure this library is sourced
: ${LIB_DIR:=$(dirname ${BASH_SOURCE[0]})}
source "$LIB_DIR/lib.source.bash"

# The unique environment variable that prevents multiple loading. Must start with LIB_
[[ -z ${LIB_GIT+x} ]] && export LIB_GIT= || return 0 # Cannot source it more than once

# How other libraries shall be sourced from a library
source "$LIB_DIR/lib.util.bash"
```

### [clean.properties.bash](scripts/clean.properties.bash)

This script is used to clean the unique environment variables from the current shell. Hence the required sourcing of it.

* ``source scripts/project/clean.properties.bash``
