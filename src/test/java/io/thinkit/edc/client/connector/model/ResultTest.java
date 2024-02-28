package io.thinkit.edc.client.connector.model;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.json.Json;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    void shouldMapWithIdentityFunction() {
        var result = new Result<>("content", null);

        var actual = result.map(it -> it);

        assertThat(actual).isNotSameAs(result);
        assertThat(actual.isSucceeded()).isTrue();
        assertThat(actual.getContent()).isEqualTo("content");
    }

    @Test
    void shouldMapWithGenericFunction() {
        var result = new Result<>("content", null);

        var actual = result.map(String::toUpperCase);

        assertThat(actual.isSucceeded()).isTrue();
        assertThat(actual.getContent()).isEqualTo("CONTENT");
    }

    @Test
    void shouldMapToDifferentType() {
        var result = new Result<>("content", null);

        var actual = result.map(it -> 42);

        assertThat(actual.isSucceeded()).isTrue();
        assertThat(actual.getContent()).isEqualTo(42);
    }

    @Test
    void shouldMapFailedResult() {
        var errors = List.of(new ApiErrorDetail(Json.createObjectBuilder().build()));
        var result = new Result<String>(null, errors);

        var actual = result.map(String::toUpperCase);

        assertThat(actual).isNotSameAs(result);
        assertThat(actual.isSucceeded()).isFalse();
        assertThat(actual.getContent()).isNull();
        assertThat(actual.getErrors()).isNotNull().isEqualTo(errors);
    }
}
