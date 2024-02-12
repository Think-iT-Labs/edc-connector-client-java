# Release

Currently, the release process is not fully automated, here's the current process described.

## Preparation

Launch the [`draft-release`](https://github.com/Think-iT-Labs/edc-connector-client-java/actions/workflows/draft-release.yml)
workflow, setting the version to be released.
At the end of the workflow there will be an open PR that wants to merge the specific version release branch into the 
`releases` one.

## Merge PR

Please ensure that you're merging the PR using the `Create merge request` and avoiding squashing or rebase.
After merging the PR, a new `publish` flow will be automatically triggered and the artifact will be uploaded to maven central.

## Tag publication

From the `releases` branch create the tag
```shell
git tag --force <version>
git push --force origin <version>
```
(note the `--force` flag is used because the tag could have been already created by mistake.

## GitHub Release

The GitHub Release can be manually drafted from the [draft release page](https://github.com/Think-iT-Labs/edc-connector-client-java/releases/new).
Here the tag `<version>` must be chosen and the release notes generated with the `Generate release notes` button.
The release will be created after clicking on the `Publish release` button.

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
