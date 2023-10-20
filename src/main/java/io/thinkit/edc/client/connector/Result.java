package io.thinkit.edc.client.connector;

public class Result<T> {
    private T content;
    private final String error;

    public Result(String error) {
        this.error = error;
    }

    public Result(T content, String error) {
        this.content = content;
        this.error = error;
    }

    public boolean isSucceeded() {
        return this.error == null;
    }

    public String getError() {
        return error;
    }

    public T getContent() {
        return content;
    }
}
