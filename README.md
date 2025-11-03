<div align="center">
  <h1>EDC Connector Client - Java 👩‍🚀 ☕ </h1>
  <p>
    <b>
      A HTTP client to communicate with the <a href="https://github.com/eclipse-edc/Connector">EDC Connector</a> for Java.
    </b>
  </p>
  <p>
      <a href="https://github.com/Think-iT-Labs/edc-connector-client-java/actions/workflows/publish-artifact.yml?query=branch%3Amain">
        <img src="https://img.shields.io/github/actions/workflow/status/Think-iT-Labs/edc-connector-client-java/publish-artifact.yml?branch=main&logo=GitHub&style=flat-square"
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

### Extending the client

The client provides a way to be extended to permit to cover additional APIs that could be added by extension on your EDC
runtime.

An extension can be implemented by extending the `io.thinkit.edc.client.connector.resource.EdcResource` class, e.g:

```java
private static class ExtensionResource extends EdcResource {

    protected ExtensionResource(EdcClientContext context) {
        super(context);
    }

    public String doStuff() {
        // here you can call your endpoint by using the HTTP client and other components available in the `context` object
        return context.httpClient().send(requestBuilder()).map(JsonLdUtil::expand).map(body -> extractDataFrom(body));
    }
}
```

To make the extension available in the client it can be added on the builder using the `with` method:
```java
var client = EdcConnectorClient.newBuilder()
        .with(ExtensionResource.class, ExtensionResource::new)
        .build();
```

and it can be obtained from the client using the `resource` method:
```
var extension = client.resource(ExtensionResource.class);

var result = extension.doStuff();
```

## Contributing

Contributions to this library are always welcome and highly encouraged

See [CONTRIBUTING](CONTRIBUTING.md) documentation for more information on how to get started.

## License

Copyright 2022-2024 Think.iT GmbH.

Licensed under the [Apache License, Version 2.0](LICENSE). Files in the project
may not be copied, modified, or distributed except according to those terms.
