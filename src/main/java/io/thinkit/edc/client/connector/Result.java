package io.thinkit.edc.client.connector;

import java.util.List;

public class Result
{
    private final boolean succeeded;
    private final String id;

    private final List<Asset> content;
    private final String error;

    public Result(boolean succeeded, String id, List<Asset> content, String error) {
        this.succeeded = succeeded;
        this.id = id;
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

    public List<Asset> getContent() {
        return content;
    }
}
