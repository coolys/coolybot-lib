#!/bin/bash

init_var() {
    result=""
    if [[ $1 != "" ]]; then
        result=$1
    elif [[ $2 != "" ]]; then
        result=$2
    fi
    echo $result
}

# uri of repo
CLB_REPO=$(init_var "$BUILD_REPOSITORY_URI" "$TRAVIS_REPO_SLUG")

# folder where the repo is cloned
CLB_CLONED=$(init_var "$BUILD_REPOSITORY_LOCALPATH" "$TRAVIS_BUILD_DIR")

# folder where the generator-coolybot is cloned
CLB_HOME="$HOME"/generator-coolybot

# folder for test-integration
CLB_INTEG="$CLB_HOME"/test-integration

# folder for samples
CLB_SAMPLES="$CLB_INTEG"/samples

# folder for scripts
CLB_SCRIPTS="$CLB_INTEG"/scripts

# folder for app
CLB_FOLDER_APP="$HOME"/app

# folder for uaa app
CLB_FOLDER_UAA="$HOME"/uaa
