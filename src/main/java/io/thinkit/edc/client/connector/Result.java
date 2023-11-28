package io.thinkit.edc.client.connector;

import java.util.List;

public class Result<T> {
    private T content;
    private final List<ApiErrorDetail> error;

    public Result(List<ApiErrorDetail> error) {
        this.error = error;
    }

    public Result(T content, List<ApiErrorDetail> error) {
        this.content = content;
        this.error = error;
    }

    public boolean isSucceeded() {
        return this.error == null;
    }

    public List<ApiErrorDetail> getError() {
        return error;
    }

    public T getContent() {
        return content;
    }
}
