package com.moonx.update;

public class UpdateResult {
    public enum Result {
        UPDATE_AVAILABLE,
        UPDATE_NOT_AVAILABLE,
        UPDATE_ERROR
    }

    private final Result result;
    private final String version;
    private final String message;

    public UpdateResult(Result result, String message) {
        this(result, message, null);
    }

    public UpdateResult(Result result, String message, String version) {
        this.result = result;
        this.message = message;
        this.version = version;
    }

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        if (version != null && result == Result.UPDATE_AVAILABLE) {
            return UpdateManager.getDownloadUrl(version);
        }
        return null;
    }
}