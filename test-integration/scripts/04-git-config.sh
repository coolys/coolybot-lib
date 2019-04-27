#!/bin/bash

set -e
source $(dirname $0)/00-init-env.sh

git config --global user.name "Coolybot Bot"
git config --global user.email "coolybot-bot@coolybot.tech"
