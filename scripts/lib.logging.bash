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

# Cannot source it more than once
[[ -z ${LIB_LOGGING+x} ]] && export LIB_LOGGING= || return 0

readonly NONE='\x1b[0m'
readonly RED='\x1b[0;31m'
readonly GREEN='\x1b[0;32m'
readonly YELLOW='\x1b[0;33m'
readonly BLUE='\x1b[0;36m'
readonly PURPLE='\x1b[0;35m'

function d { date "+%Y/%m/%d %H:%M:%S.%N%z" | sed -E 's/([0-9]{3})[0-9]{6}/\1/' ; }
function do_log { 1>&2 echo -e "${PURPLE}[$PWD]${NONE} $(d) $1[$2]${NONE} ${@:3}" ; }

function log__error { do_log "${RED}"    "ERROR" $@ ; }
function log__warn  { do_log "${YELLOW}" "WARN " $@ ; }
function log__info  { do_log "${GREEN}"  "INFO " $@ ; }
function log__debug { do_log "${BLUE}"   "DEBUG" $@ ; }
