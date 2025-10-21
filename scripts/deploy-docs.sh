#!/usr/bin/env bash

# Get the latest tag
version=$(git tag --sort=-taggerdate | head -1)

echo "Publishing documentation for version $version"
sbt makeSite
mv -v documentation/target/site ../releasing-the-docs

git checkout gh-pages
git pull

rm -rf *
mv -v ../releasing-the-docs docs
mv -v docs/* .
rm -rfv docs
touch .nojekyll
git add .
git reset HEAD .idea .bsp
git commit -m "Releasing docs for version $version"
git push

echo "Docs where published. Getting back to release branch."
git checkout -

echo "[DOCS PUSHED]"
echo "(Make sure to update mimaPreviousArtifacts)"
