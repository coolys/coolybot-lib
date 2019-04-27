#!/bin/bash

set -e
source $(dirname $0)/00-init-env.sh

if [[ $CLB_VERSION == '' ]]; then
    CLB_VERSION=0.0.0-CICD
fi

# artifact version of coolybot-parent
sed -e '/<artifactId>coolybot-parent<\/artifactId>/{N;s/<version>.*<\/version>/<version>'$CLB_VERSION'<\/version>/1;}' pom.xml > pom.xml.sed
mv -f pom.xml.sed pom.xml

# coolybot-framework.version property in coolybot-parent
sed -e 's/<coolybot-framework.version>.*<\/coolybot-framework.version>/<coolybot-framework.version>'$CLB_VERSION'<\/coolybot-framework.version>/1' pom.xml > pom.xml.sed
mv -f pom.xml.sed pom.xml

# parent version of coolybot-dependencies
sed -e '/<artifactId>coolybot-parent<\/artifactId>/{N;s/<version>.*<\/version>/<version>'$CLB_VERSION'<\/version>/1;}' coolybot-dependencies/pom.xml > coolybot-dependencies/pom.xml.sed
mv -f coolybot-dependencies/pom.xml.sed coolybot-dependencies/pom.xml

# parent version of coolybot-framework
sed -e '/<artifactId>coolybot-dependencies<\/artifactId>/{N;s/<version>.*<\/version>/<version>'$CLB_VERSION'<\/version>/1;}' coolybot-framework/pom.xml > coolybot-framework/pom.xml.sed
mv -f coolybot-framework/pom.xml.sed coolybot-framework/pom.xml
