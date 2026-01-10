package com.moonx.update;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateDownloader {

    public static boolean download(UpdateResult result) {
        if (!result.hasUpdate()) return false;

        try {
            URL url = new URL(result.getDownloadUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(30000);

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            File dir = new File("downloads");
            if (!dir.exists()) dir.mkdirs();

            File output = new File(
                    dir,
                    UpdateResult.CORE_NAME + result.getVersion() + ".jar"
            );

            try (InputStream in = con.getInputStream();
                 FileOutputStream out = new FileOutputStream(output)) {

                byte[] buffer = new byte[4096];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}