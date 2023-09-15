package io.thinkit.edc.client.connector;

public class Result
{
    private final boolean succeeded;
    private final String id;
    private final String error;

    public Result(boolean succeeded, String id, String error) {
        this.succeeded = succeeded;
        this.id = id;
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
}
