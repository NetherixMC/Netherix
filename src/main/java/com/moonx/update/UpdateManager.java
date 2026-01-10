package com.moonx.update;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {

    private static String CURRENT_VERSION;

    public UpdateManager() {
        if (CURRENT_VERSION == null) {
            CURRENT_VERSION = readCurrentVersion();
        }
    }

    private String readCurrentVersion() {
        try (InputStream is = getClass().getResourceAsStream("/version.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.readLine().trim();
        } catch (Exception e) {
            return "1.0.0";
        }
    }

    public static String getCurrentVersion() {
        return CURRENT_VERSION;
    }

    private String fetchLatestVersion() throws Exception {
        URL url = new URL(UpdateResult.VERSION_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in =
                         new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                return in.readLine().trim();
            }
        }
        return null;
    }

    public UpdateResult checkForUpdates() {
        try {
            String latest = fetchLatestVersion();

            if (latest == null) {
                return new UpdateResult(
                        UpdateResult.Result.UPDATE_ERROR,
                        "Gagal mengambil versi terbaru"
                );
            }

            if (isNewerVersion(latest, CURRENT_VERSION)) {
                return new UpdateResult(
                        UpdateResult.Result.UPDATE_AVAILABLE,
                        "Versi baru tersedia: " + latest,
                        latest
                );
            }

            return new UpdateResult(
                    UpdateResult.Result.UPDATE_NOT_AVAILABLE,
                    "Sudah menggunakan versi terbaru (" + CURRENT_VERSION + ")",
                    CURRENT_VERSION
            );

        } catch (Exception e) {
            return new UpdateResult(
                    UpdateResult.Result.UPDATE_ERROR,
                    "Error update: " + e.getMessage()
            );
        }
    }

    private boolean isNewerVersion(String latest, String current) {
        String[] l = latest.split("\\.");
        String[] c = current.split("\\.");

        int max = Math.max(l.length, c.length);
        for (int i = 0; i < max; i++) {
            int lv = i < l.length ? Integer.parseInt(l[i]) : 0;
            int cv = i < c.length ? Integer.parseInt(c[i]) : 0;

            if (lv > cv) return true;
            if (lv < cv) return false;
        }
        return false;
    }
}