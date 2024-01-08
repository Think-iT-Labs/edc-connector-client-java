# Release

Currently the release process is not fully automated, here's the current process described step by step used to release 
version `0.0.1`

## Preparation

Creation of the release branch and update version to `0.0.1`

```shell
git checkout -b release/0.0.1
sed -i 's/version=.*/version=0.0.1/g' gradle.properties
git add .
git commit -m "Prepare release 0.0.1"
git push origin release/0.0.1
```

## Pull request

After push, a pull request was manually created, from `release/0.0.1` to `releases`.
After merge, a new `publish` flow has been automatically triggered and the artifact has been uploaded to maven central.

## Tag publication

From the `releases` branch the tag has been created and pushed:
```shell
git tag --force 0.0.1
git push --force origin 0.0.1
```
(note the `--force` flag has been used because the tag could have been already created by mistake

## GitHub Release

The GitHub Release has been manually drafted from the [draft release page](https://github.com/Think-iT-Labs/edc-connector-client-java/releases/new).
Here the tag must be chosen and the release notes generated with the `Generate release notes` button.
The release has been created after clicking on the `Publish release` button.

## Prepare next SNAPSHOT version

From the terminal (that's still on the HEAD of the `releases` branch):
```shell
git checkout main && git merge -X theirs releases --no-commit --no-ff
sed -i "s/version=.*/version=0.0.2-SNAPSHOT/g" gradle.properties
git add gradle.properties
git commit --message "Introduce new snapshot version 0.0.2-SNAPSHOT"
git push origin main
```

Artifacts:
 - [maven central](https://s01.oss.sonatype.org/#nexus-search;gav~io.think-it~edc-connector-client~0.0.1~~)
 - [github release](https://github.com/Think-iT-Labs/edc-connector-client-java/releases/tag/0.0.1)
