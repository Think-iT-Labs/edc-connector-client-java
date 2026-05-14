package io.thinkit.edc.client.connector.resource;

public record VersionedApi(String url, String version) {

    public String baseUrl() {
        return url + "/" + version;
    }
}
