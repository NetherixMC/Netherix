package com.moonx.update;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

public class UpdateManager {
    private static String CURRENT_VERSION;
    private static final String VERSION_URL = "https://raw.githubusercontent.com/username/repository/main/version.txt";
    private static final String DOWNLOAD_BASE_URL = "https://github.com/username/repository/releases/download/v";

    public UpdateManager() {
        if (CURRENT_VERSION == null) {
            CURRENT_VERSION = getCurrentVersionFromFile();
        }
    }

    /**
     * Baca versi dari file version.txt di dalam JAR
     */
    private String getCurrentVersionFromFile() {
        try (InputStream is = getClass().getResourceAsStream("/version.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.readLine().trim();
        } catch (Exception e) {
            // Jika gagal, gunakan versi default
            return "1.0.0";
        }
    }

    public static String getCurrentVersion() {
        return CURRENT_VERSION;
    }

    /**
     * Ambil versi terbaru dari server
     */
    private String fetchLatestVersion() throws Exception {
        URL url = new URL(VERSION_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                return in.readLine().trim();
            }
        }
        return null;
    }

    /**
     * Cek apakah ada update tersedia
     */
    public UpdateResult checkForUpdates() {
        try {
            String latestVersion = fetchLatestVersion();

            if (latestVersion == null) {
                return new UpdateResult(
                        UpdateResult.Result.UPDATE_ERROR,
                        "Gagal memeriksa versi terbaru",
                        null
                );
            }

            if (isNewerVersion(latestVersion, CURRENT_VERSION)) {
                return new UpdateResult(
                        UpdateResult.Result.UPDATE_AVAILABLE,
                        "Versi terbaru " + latestVersion + " tersedia!",
                        latestVersion
                );
            } else {
                return new UpdateResult(
                        UpdateResult.Result.UPDATE_NOT_AVAILABLE,
                        "Anda sudah menggunakan versi terbaru (" + CURRENT_VERSION + ")",
                        CURRENT_VERSION
                );
            }
        } catch (Exception e) {
            return new UpdateResult(
                    UpdateResult.Result.UPDATE_ERROR,
                    "Gagal memeriksa pembaruan: " + e.getMessage(),
                    null
            );
        }
    }

    /**
     * Bandingkan versi: apakah latestVersion lebih baru dari currentVersion
     */
    private boolean isNewerVersion(String latestVersion, String currentVersion) {
        String[] latest = latestVersion.split("\\.");
        String[] current = currentVersion.split("\\.");

        int length = Math.max(latest.length, current.length);
        for (int i = 0; i < length; i++) {
            int latestPart = i < latest.length ? Integer.parseInt(latest[i]) : 0;
            int currentPart = i < current.length ? Integer.parseInt(current[i]) : 0;

            if (latestPart > currentPart) {
                return true;
            } else if (latestPart < currentPart) {
                return false;
            }
        }
        return false;
    }

    /**
     * Generate URL download untuk versi tertentu
     */
    public static String getDownloadUrl(String version) {
        return DOWNLOAD_BASE_URL + version + "/Netherix-" + version + ".jar";
    }

    /**
     * Download update versi baru
     */
    public static boolean downloadUpdate(String version) {
        try {
            String downloadUrl = getDownloadUrl(version);
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000); // 30 detik untuk download

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Buat folder downloads jika belum ada
                File downloadsDir = new File("downloads");
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdir();
                }

                // Simpan file
                String fileName = "Netherix-" + version + ".jar";
                File outputFile = new File(downloadsDir, fileName);

                try (InputStream in = connection.getInputStream();
                     FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}