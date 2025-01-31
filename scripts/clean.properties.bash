#!/bin/bash

function check_sourced {
    if [[ "${FUNCNAME[1]}" != "source" ]]; then
        1>&2 echo "This script should be sourced, but is not. StackTrace:"
        for index in ${!BASH_SOURCE[@]} ; do
            1>&2 echo "    [$index] ${BASH_SOURCE[$index]}:${FUNCNAME[$index]}"
        done
        echo
        echo "Expected: source ${BASH_SOURCE[0]}"
        exit 1
    fi
}
check_sourced

LIB_DIR="$(dirname "${BASH_SOURCE[0]}")"
for var in $(grep -Eoh "export[ ]*LIB_[_0-9A-Z]*" "$LIB_DIR/"lib.* | sort -u | sed 's,export[ ]*,,') ; do
    echo unset $var ; unset $var
done
