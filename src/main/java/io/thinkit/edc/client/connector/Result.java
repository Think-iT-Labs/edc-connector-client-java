package io.thinkit.edc.client.connector;

import java.util.List;

public class Result<T>
{
    private final boolean succeeded;
    private T content;
    private final String error;

    public Result(boolean succeeded, String error) {
        this.succeeded = succeeded;
        this.error = error;
    }

    public Result(boolean succeeded, T content, String error) {
        this.succeeded = succeeded;
        this.content = content;
        this.error = error;
    }

    public boolean isSucceeded(){
        return this.succeeded;
    }
    public String getError() {
        return error;
    }

    public T getContent() {
        return content;
    }
}
