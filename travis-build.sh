#!/usr/bin/env bash

set -e

[[ -d target ]] && rm -rf target
rm -rf *.zip
chmod +x ./grailsw
./grailsw refresh-dependencies --non-interactive
./grailsw compile --non-interactive
./grailsw codenarc --non-interactive
./grailsw test-app --non-interactive
./grailsw package-plugin --non-interactive

if [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_REPO_SLUG == "enr/grails-spring-security-oauth" && $TRAVIS_PULL_REQUEST == 'false' ]]; then
  ./grailsw publish-plugin --no-scm --allow-overwrite --non-interactive
else
  echo "Not on master branch, so not publishing"
  echo "TRAVIS_BRANCH: $TRAVIS_BRANCH"
  echo "TRAVIS_REPO_SLUG: $TRAVIS_REPO_SLUG"
  echo "TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
fi
