<div align="center">
  <h1>EDC Connector Client - Java 👩‍🚀 ☕ </h1>
  <p>
    <b>
      A HTTP client to communicate with the <a href="https://github.com/eclipse-edc/Connector">EDC Connector</a> for Java.
    </b>
  </p>
  <p>
      <a href="https://github.com/Think-iT-Labs/edc-connector-client-java/actions/workflows/publish.yml?query=branch%3Amain">
        <img src="https://img.shields.io/github/actions/workflow/status/Think-iT-Labs/edc-connector-client-java/publish.yml?branch=main&logo=GitHub&style=flat-square"
        alt="Tests status" />
      </a>
  </p>
  <sub>
    Built with ❤️ at <a href="https://think-it.io">Think-it</a>.
  </sub>
</div>

## Description

The EDC Connector Client library for Java is a simple, easy and with minimum dependencies amount library to interact with 
[EDC Connector Management API](https://app.swaggerhub.com/apis/eclipse-edc-bot/management-api)

Features:
 - JSON-LD expansion/compaction processes done under the hood of the class model.
 - Straightforward error handling.
 - Configurable http interceptors to customize the calls made to the connector.

## Requirements
- Java 17 or higher

## Installation

The library is published on [maven central](https://central.sonatype.com/artifact/io.think-it/edc-connector-client)
Choose the latest version and add it as a dependency in your favorite build automation tool.

## Usage
Everything evolves around the `EdcConnectorClient` that can be instantiated through the `Builder`:
```java
var client = EdcConnectorClient.newBuilder()
    .managementUrl("http://your.connector/management/api")
    .build();
```

From that `client` object the context specific endpoint services can be obtained:
- `assets()`
- `policyDefinitions()`
- `contractDefinitions()`
- `contractNegotiations()`
- `contractAgreements()`
- `transferProcesses()`
- `dataplanes()`
- `applicationObservability()`
- `catalogs()`

## Contributing

Contributions to this library are always welcome and highly encouraged

See [CONTRIBUTING](CONTRIBUTING.md) documentation for more information on how to get started.

## License

Copyright 2022-2024 Think.iT GmbH.

Licensed under the [Apache License, Version 2.0](LICENSE). Files in the project
may not be copied, modified, or distributed except according to those terms.
