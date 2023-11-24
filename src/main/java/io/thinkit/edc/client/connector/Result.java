package io.thinkit.edc.client.connector;

public class Result<T> {
    private T content;
    private final ApiErrorDetail error;

    public Result(ApiErrorDetail error) {
        this.error = error;
    }

    public Result(T content, ApiErrorDetail error) {
        this.content = content;
        this.error = error;
    }

    public boolean isSucceeded() {
        return this.error == null;
    }

    public ApiErrorDetail getError() {
        return error;
    }

    public T getContent() {
        return content;
    }
}
