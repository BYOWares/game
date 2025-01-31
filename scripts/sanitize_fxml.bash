#!/bin/bash

# Make sure the xmlns declaring the javafx version is inline with the version
# used inside the project, thus preventing warning log while loading FXML.
# The Java Version is used, because we use it as well for the JavaFX version.
# See file: buildSrc/src/main/kotlin/byogame.java-fx.gradle.kts

source "$(dirname ${BASH_SOURCE[0]})/lib.git.bash"

IFS=$'\t\n';

git__move_to_root_dir || exit

JAVA_VERSION_FILE="buildSrc/src/main/kotlin/byogame.java-common.gradle.kts"
[[ ! -f "$JAVA_VERSION_FILE" ]] && log__error "'$JAVA_VERSION_FILE' does not exist" && exit 1

JAVA_VERSION="$(cat "$JAVA_VERSION_FILE" | grep JavaLanguageVersion | grep -Eo "[0-9]*")"
if [[ "$JAVA_VERSION" =~ ^[0-9]+$ ]] ; then
    log__info "Java version found: $JAVA_VERSION"
    FXML=($(git ls-files | grep '\.fxml$'))
    for f in "${FXML[@]}" ; do
        log__info "Sanitizing $f ..."
        sed -i -r 's,(xmlns="http://javafx.com/javafx/)[^"]*,\1'"$JAVA_VERSION," $f
    done
else
    log__error "Could not find the java version in file '$JAVA_VERSION_FILE'"
    exit 1
fi
