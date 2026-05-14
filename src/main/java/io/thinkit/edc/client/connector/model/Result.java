package io.thinkit.edc.client.connector.model;

import java.util.List;
import java.util.function.Function;

public class Result<T> {
    private T content;
    private final List<ApiErrorDetail> errors;

    public static <T> Result<T> succeded(T content) {
        return new Result<>(content, null);
    }

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

    public <R> Result<R> map(Function<T, R> mappingFunction) {
        if (isSucceeded()) {
            return new Result<>(mappingFunction.apply(this.content), null);
        } else {
            return new Result<>(null, this.errors);
        }
    }

    public <R> Result<R> compose(Function<T, Result<R>> mappingFunction) {
        if (isSucceeded()) {
            return mappingFunction.apply(this.content);
        } else {
            return new Result<>(null, this.errors);
        }
    }

    public <R> Result<R> flatMap(Function<Result<T>, Result<R>> mappingFunction) {
        return mappingFunction.apply(this);
    }
}
