package io.thinkit.edc.client.connector;

import java.util.List;

public class Result<T> {
    private T content;
    private final List<ApiErrorDetail> errors;

    public Result(List<ApiErrorDetail> error) {
        this.errors = error;
    }

    public Result(T content, List<ApiErrorDetail> error) {
        this.content = content;
        this.errors = error;
    }

    public boolean isSucceeded() {
        return this.errors == null;
    }

    public List<ApiErrorDetail> getErrors() {
        return errors;
    }

    public T getContent() {
        return content;
    }
}
