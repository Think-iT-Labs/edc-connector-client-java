package io.thinkit.edc.client.connector.services.management;

import static io.thinkit.edc.client.connector.utils.JsonLdUtil.compact;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

import com.fasterxml.jackson.core.type.TypeReference;
import io.thinkit.edc.client.connector.EdcClientContext;
import io.thinkit.edc.client.connector.model.ContractAgreement;
import io.thinkit.edc.client.connector.model.ContractNegotiation;
import io.thinkit.edc.client.connector.model.QuerySpec;
import io.thinkit.edc.client.connector.model.Result;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractAgreement;
import io.thinkit.edc.client.connector.model.jsonld.JsonLdContractNegotiation;
import io.thinkit.edc.client.connector.model.pojo.PojoContractAgreement;
import io.thinkit.edc.client.connector.model.pojo.PojoContractNegotiation;
import io.thinkit.edc.client.connector.resource.management.ManagementResource;
import jakarta.json.JsonArray;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ContractAgreements extends ManagementResource {
    private final String url;

    public ContractAgreements(EdcClientContext context) {
        super(context);
        url = "%s/contractagreements".formatted(managementUrl);
    }

    public Result<ContractAgreement> get(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractAgreement, deserializeContractAgreement());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<ContractAgreement>> getAsync(String id) {
        var requestBuilder = getContractAgreementRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractAgreement, deserializeContractAgreement());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<ContractNegotiation> getNegotiation(String id) {
        var requestBuilder = getContractNegotiationRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractNegotiation, deserializeContractNegotiation());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<ContractNegotiation>> getNegotiationAsync(String id) {
        var requestBuilder = getContractNegotiationRequestBuilder(id);
        var deserialize = responseDeserializer(this::getContractNegotiation, deserializeContractNegotiation());

        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    public Result<List<ContractAgreement>> request(QuerySpec input) {
        var requestBuilder = ContractAgreementsRequestBuilder(input);
        var deserialize = responseDeserializer(this::getContractAgreements, deserializeContractAgreements());
        return context.httpClient().send(requestBuilder).flatMap(deserialize);
    }

    public CompletableFuture<Result<List<ContractAgreement>>> requestAsync(QuerySpec input) {
        var requestBuilder = ContractAgreementsRequestBuilder(input);
        var deserialize = responseDeserializer(this::getContractAgreements, deserializeContractAgreements());
        return context.httpClient().sendAsync(requestBuilder).thenApply(deserialize);
    }

    private HttpRequest.Builder getContractAgreementRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder getContractNegotiationRequestBuilder(String id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/%s/negotiation".formatted(this.url, id)))
                .GET();
    }

    private HttpRequest.Builder ContractAgreementsRequestBuilder(QuerySpec input) {
        var requestBody = compact(input);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s/request".formatted(this.url)))
                .header("content-type", "application/json")
                .POST(ofString(requestBody.toString()));
    }

    private ContractAgreement getContractAgreement(JsonArray array) {
        return JsonLdContractAgreement.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private ContractNegotiation getContractNegotiation(JsonArray array) {
        return JsonLdContractNegotiation.Builder.newInstance()
                .raw(array.getJsonObject(0))
                .build();
    }

    private List<ContractAgreement> getContractAgreements(JsonArray array) {
        return array.stream()
                .map(s -> JsonLdContractAgreement.Builder.newInstance()
                        .raw(s.asJsonObject())
                        .build())
                .map(ContractAgreement.class::cast)
                .toList();
    }

    private Function<InputStream, List<ContractAgreement>> deserializeContractAgreements() {
        return stream -> {
            try {
                return context
                        .objectMapper()
                        .readValue(stream, new TypeReference<List<PojoContractAgreement>>() {})
                        .stream()
                        .map(ContractAgreement.class::cast)
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, ContractAgreement> deserializeContractAgreement() {
        return stream -> {
            try {
                byte[] bytes = stream.readAllBytes();
                String json = new String(bytes, StandardCharsets.UTF_8);

                // Print/Log the value
                System.out.println("Stream Content: " + json);
                return context.objectMapper().readValue(stream, PojoContractAgreement.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<InputStream, ContractNegotiation> deserializeContractNegotiation() {
        return stream -> {
            try {
                byte[] bytes = stream.readAllBytes();
                String json = new String(bytes, StandardCharsets.UTF_8);

                // Print/Log the value
                System.out.println("Stream Content: " + json);
                return context.objectMapper().readValue(stream, PojoContractNegotiation.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
