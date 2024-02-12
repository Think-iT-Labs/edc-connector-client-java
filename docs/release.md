# Release

Currently, the release process is not fully automated, here's the current process described.

## Preparation

Launch the [`draft-release`](https://github.com/Think-iT-Labs/edc-connector-client-java/actions/workflows/draft-release.yml)
workflow, setting the version to be released.
At the end of the workflow there will be an open PR that wants to merge the specific version release branch into the 
`releases` one.

## Merge PR

After merging the PR, a new `release` flow will be automatically triggered that will do everything that's needed:
- publish artifact
- tag version
- create GitHub release
- prepare next SNAPSHOT version

Artifacts:
 - [maven central](https://central.sonatype.com/artifact/io.think-it/edc-connector-client)
 - [github releases](https://github.com/Think-iT-Labs/edc-connector-client-java/releases/)
