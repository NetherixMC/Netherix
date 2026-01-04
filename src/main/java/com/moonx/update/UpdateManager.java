package com.moonx.update;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UpdateManager {
    private static String CURRENT_VERSION;
    private static final String VERSION_URL = "https://raw.githubusercontent.com/username/repository/main/version.txt";
    private static final String DOWNLOAD_BASE_URL = "https://github.com/username/repository/releases/download/v";
    private static final String DOWNLOAD_BASE_SOFTWARE_URL = "https://github.com/NetherixMC/Netherix";

    public UpdateManager() {
        this.CURRENT_VERSION = getCurrentVersionFromFile();
    }

    private String getCurrentVersionFromFile() {
        try {
            // Coba baca versi dari file version.txt di JAR
            try (InputStream is = getClass().getResourceAsStream("/version.txt");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.readLine().trim();
            }
        } catch (Exception e) {
            // Jika gagal, gunakan versi default
            return "1.0";
        }
    }

    public static String getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public static String getLatestVersion() throws IOException {
        URL url = new URL(VERSION_URL);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return reader.readLine().trim();
        }
    }

    public static String getDownloadUrl(String version) {
        return DOWNLOAD_BASE_URL + version + "/Netherix-" + version + ".jar";
    }

    public static boolean downloadUpdate(String version) {
        try {
            String downloadUrl = getDownloadUrl(version);
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000); // 30 seconds for download

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Create downloads directory if it doesn't exist
                File downloadsDir = new File("downloads");
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdir();
                }

                // Save the file
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

            // Place the code here
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

    private String fetchLatestVersion() throws Exception {
        URL url = new URL(VERSION_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                return in.readLine().trim();
            }
        }
        return null;
    }

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

    // UPDATE

}