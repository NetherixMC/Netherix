package com.moonx.update;

public class UpdateResult {

    /* ========= KONSTANTA ========= */

    public static final String CORE_NAME = "Netherix-Core";
    public static final String VERSION_URL =
            "https://raw.githubusercontent.com/NetherixMC/Netherix-Core/main/version.txt";
    public static final String DOWNLOAD_BASE_URL =
            "https://github.com/NetherixMC/Netherix-Core/releases/download/v";

    /* ========= STATUS ========= */

    public enum Result {
        UPDATE_AVAILABLE,
        UPDATE_NOT_AVAILABLE,
        UPDATE_ERROR
    }

    private final Result result;
    private final String message;
    private final String version;

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

    /* ========= HELPER ========= */

    public boolean hasUpdate() {
        return result == Result.UPDATE_AVAILABLE && version != null;
    }

    public String getDownloadUrl() {
        if (!hasUpdate()) return null;
        return DOWNLOAD_BASE_URL + version + "/" + CORE_NAME + version + ".jar";
    }
}