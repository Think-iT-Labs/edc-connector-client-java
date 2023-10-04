package io.thinkit.edc.client.connector;

import java.util.List;

public class Result<T>
{
    private final boolean succeeded;
    private String id;

    private List<T> content;
    private final String error;

    public Result(boolean succeeded, String error) {
        this.succeeded = succeeded;
        this.error = error;
    }

    public Result(boolean succeeded, String id, String error) {
        this.succeeded = succeeded;
        this.id = id;
        this.error = error;
    }

    public Result(boolean succeeded, List<T> content, String error) {
        this.succeeded = succeeded;
        this.content = content;
        this.error = error;
    }

    public boolean isSucceeded(){
        return this.succeeded;
    }
    public String getId(){
        return this.id;
    }
    public String getError() {
        return error;
    }

    public List<T> getContent() {
        return content;
    }
}
