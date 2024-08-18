#!/bin/bash

set -ex

# Generate the API docs
./gradlew dokkaHtmlMultiModule

mkdir -p docs/api
mv build/dokka/htmlMultiModule/* docs/api

# Copy in special files that GitHub wants in the project root.
GITHUB_URL=https://github.com/jenly1314/UltraSwipeRefresh/
echo $GITHUB_URL
sed "/<!-- end -->/q" README.md > docs/index.md
# sed -i "s|app/src/main/ic_launcher-web.png|ic_logo.png|g" docs/index.md
sed -i "s|](app|](${GITHUB_URL}blob/master/app|g" docs/index.md
sed -i "s|](art|](${GITHUB_URL}blob/master/art|g" docs/index.md
sed -i "s|](refresh|](${GITHUB_URL}blob/master/refresh|g" docs/index.md
cat CHANGELOG.md | grep -v '## 版本日志' > docs/changelog.md

cp -r  art docs/art
# cp app/src/main/ic_launcher-web.png docs/ic_logo.png

# Build the site locally
mkdocs build
