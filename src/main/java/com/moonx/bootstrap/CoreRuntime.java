package com.moonx.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class CoreRuntime {

    private static Process coreProcess;

    public static void startCore() {

        // 📁 Folder khusus core
        File coreDir = new File(".");

        File coreJar = findCoreJar(coreDir);
        if (coreJar == null) {
            System.err.println("Warning: Netherix Core not found.");
            System.exit(1);
        }

        List<String> command = new ArrayList<>();
        command.add("java");

        command.add("-jar");

        // 🔑 KUNCI AKSES
        command.add("-Dlauncher=netherix");

        command.add(coreJar.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.inheritIO(); // ✅ Penting: inherit IO agar GUI bisa muncul

        try {
            coreProcess = pb.start();
            System.out.println("Core started: " + coreJar.getName());
            
            // ✅ Setup shutdown hook untuk stop core saat JVM mati
            Runtime.getRuntime().addShutdownHook(new Thread(CoreRuntime::stopCore));
            
            // ✅ Monitor output core (opsional, untuk debug)
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(coreProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[CORE] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            
            // ✅ Tunggu core process selesai (blocking)
            int exitCode = coreProcess.waitFor();
            System.out.println("Core exited with code: " + exitCode);
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Warning: Cannot start core.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void stopCore() {
        if (coreProcess != null && coreProcess.isAlive()) {
            System.out.println("Stopping Netherix Core...");
            coreProcess.destroy();
            try {
                coreProcess.waitFor(); // Tunggu sampai benar-benar mati
            } catch (InterruptedException e) {
                coreProcess.destroyForcibly(); // Paksa kill jika tidak mau mati
            }
        }
    }

    // 🔍 SCAN CORE VIA MANIFEST
    private static File findCoreJar(File dir) {
        if (!dir.exists() || !dir.isDirectory()) return null;

        File[] files = dir.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (!file.getName().endsWith(".jar")) continue;

            try (JarFile jar = new JarFile(file)) {
                Manifest mf = jar.getManifest();
                if (mf == null) continue;

                String type = mf.getMainAttributes()
                        .getValue("Netherix-Type");

                if ("Core".equals(type)) {
                    return file; // ✅ CORE VALID
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}