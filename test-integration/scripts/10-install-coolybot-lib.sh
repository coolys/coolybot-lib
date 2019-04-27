#!/bin/bash

set -e
source $(dirname $0)/00-init-env.sh

#-------------------------------------------------------------------------------
# Install Coolybot Dependencies and Server-side library
#-------------------------------------------------------------------------------
cd "$HOME"
if [[ "$CLB_REPO" == *"/coolybot" ]]; then
    echo "*** coolybot: use local version at CLB_REPO=$CLB_REPO"

    cd "$CLB_CLONED"
    git --no-pager log -n 10 --graph --pretty='%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit

    test-integration/scripts/10-replace-version-coolybot.sh

    ./mvnw clean install -Dgpg.skip=true
    ls -al ~/.m2/repository/io/github/coolys/coolybot-framework/
    ls -al ~/.m2/repository/io/github/coolys/coolybot-dependencies/
    ls -al ~/.m2/repository/io/github/coolys/coolybot-parent/

elif [[ "$CLB_LIB_BRANCH" == "release" ]]; then
    echo "*** coolybot: use release version"

else
    echo "*** coolybot: CLB_LIB_REPO=$CLB_LIB_REPO with CLB_LIB_BRANCH=$CLB_LIB_BRANCH"
    git clone "$CLBPSTER_LIB_REPO" coolybot
    cd coolybot
    if [ "$CLB_LIB_BRANCH" == "latest" ]; then
        LATEST=$(git describe --abbrev=0)
        git checkout "$LATEST"
    elif [ "$CLB_LIB_BRANCH" != "master" ]; then
        git checkout "$CLB_LIB_BRANCH"
    fi
    git --no-pager log -n 10 --graph --pretty='%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit

    test-integration/scripts/10-replace-version-coolybot.sh

    ./mvnw clean install -Dgpg.skip=true
    ls -al ~/.m2/repository/io/github/coolybot/coolybot-framework/
    ls -al ~/.m2/repository/io/github/coolybot/coolybot-dependencies/
    ls -al ~/.m2/repository/io/github/coolybot/coolybot-parent/
fi

#-------------------------------------------------------------------------------
# Install Coolybot Generator
#-------------------------------------------------------------------------------
cd "$HOME"
if [[ "$CLB_REPO" == *"/generator-coolybot" ]]; then
    echo "*** generator-coolybot: use local version at CLB_REPO=$CLB_REPO"

    cd "$CLB_HOME"
    git --no-pager log -n 10 --graph --pretty='%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit

    npm ci
    npm install -g "$CLB_HOME"
    if [[ "$CLB_APP" == "" || "$CLB_APP" == "ngx-default" ]]; then
        npm test
    fi

elif [[ "$CLB_GEN_BRANCH" == "release" ]]; then
    echo "*** generator-coolybot: use release version"
    npm install -g generator-coolybot

else
    echo "*** generator-coolybot: CLB_GEN_REPO=$CLB_GEN_REPO with CLB_GEN_BRANCH=$CLB_GEN_BRANCH"
    git clone "$CLB_GEN_REPO" generator-coolybot
    cd generator-coolybot
    if [ "$CLB_GEN_BRANCH" == "latest" ]; then
        LATEST=$(git describe --abbrev=0)
        git checkout "$LATEST"
    elif [ "$CLB_GEN_BRANCH" != "master" ]; then
        git checkout "$CLB_GEN_BRANCH"
    fi
    git --no-pager log -n 10 --graph --pretty='%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit

    npm ci
    npm install -g "$HOME"/generator-coolybot
fi

#-------------------------------------------------------------------------------
# Override config
#-------------------------------------------------------------------------------

# replace 00-init-env.sh
cp "$CLB_CLONED"/test-integration/scripts/00-init-env.sh "$CLB_HOME"/test-integration/scripts/
