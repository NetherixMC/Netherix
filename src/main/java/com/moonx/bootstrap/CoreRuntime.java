package com.moonx.bootstrap;

import com.moonx.gui.UpdateGUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static com.moonx.update.UpdateResult.CORE_NAME;

public final class CoreRuntime {

    private static Process coreProcess;

    /* =========================================================
     * ENTRY POINT
     * ========================================================= */
    public static void startCore() {
        File coreJar = locateOrDownloadCoreOrExit();
        startCoreProcess(coreJar);
    }

    /* =========================================================
     * CORE LOCATOR + AUTO DOWNLOAD
     * ========================================================= */
    private static File locateOrDownloadCoreOrExit() {
        File coreJar = new File(CORE_NAME);

        // 1. Core ada → langsung pakai
        if (coreJar.exists() && coreJar.isFile()) {
            return coreJar;
        }

        // 2. Scan manifest (jika nama berubah)
        System.err.println("[Runtime] Core tidak ditemukan, scan manifest...");
        coreJar = findCoreJarByManifest(new File("."));

        if (coreJar != null) {
            System.out.println("[Runtime] Core ditemukan melalui manifest: " + coreJar.getName());
            return coreJar;
        }

        // 3. Tidak ada → download
        System.out.println("[Runtime] Core tidak ditemukan, mengunduh...");
        downloadCoreOrExit();

        // 4. Validasi hasil download
        File downloaded = new File(CORE_NAME);
        if (!downloaded.exists() || !downloaded.isFile()) {
            fatal("Core gagal diunduh atau file tidak valid");
        }

        return downloaded;
    }

    private static File findCoreJarByManifest(File dir) {
        if (!dir.isDirectory()) return null;

        File[] jars = dir.listFiles((d, name) -> name.endsWith(".jar"));
        if (jars == null) return null;

        File found = null;

        for (File jar : jars) {
            try (JarFile jf = new JarFile(jar)) {
                Manifest mf = jf.getManifest();
                if (mf == null) continue;

                Attributes a = mf.getMainAttributes();
                String type = a.getValue("Netherix-Type");

                if ("Core".equalsIgnoreCase(type)) {
                    if (found != null) {
                        fatal("Lebih dari satu Core terdeteksi (ambigu)");
                    }
                    found = jar;
                }
            } catch (IOException ignored) {
            }
        }
        return found;
    }

    /* =========================================================
     * CORE DOWNLOAD
     * ========================================================= */
    private static void downloadCoreOrExit() {
        try {
            // ⬇⬇⬇ HUBUNGKAN KE SISTEM UPDATE KAMU ⬇⬇⬇
            // Contoh:
            UpdateGUI.checkAndShowUpdates();

            System.out.println("[Runtime] Mengunduh netherix-core.jar...");
            // ---- SIMULASI / STUB ----
            Thread.sleep(1000);
            // -------------------------

            System.out.println("[Runtime] Download Core selesai");
        } catch (Exception e) {
            fatal("Gagal mengunduh Core", e);
        }
    }

    /* =========================================================
     * PROCESS START
     * ========================================================= */
    private static void startCoreProcess(File coreJar) {
        try {
            long runtimePid = ProcessHandle.current().pid();

            List<String> cmd = new ArrayList<>();
            cmd.add("java");
            cmd.add("-jar");
            cmd.add("-Dlauncher=netherix");
            cmd.add(coreJar.getName());
            cmd.add("--parent-pid=" + runtimePid);

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(coreJar.getParentFile());
            pb.redirectErrorStream(true);

            coreProcess = pb.start();
            System.out.println("System RUN");

            Runtime.getRuntime().addShutdownHook(new Thread(CoreRuntime::stopCore));
            monitorCoreProcess();

        } catch (IOException e) {
            fatal("Gagal memulai Core", e);
        }
    }

    /* =========================================================
     * PROCESS MONITOR
     * ========================================================= */
    private static void monitorCoreProcess() {
        new Thread(() -> {
            try {
                int code = coreProcess.waitFor();
                System.out.println("[Runtime] Core exited with code: " + code);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Core-Monitor").start();
    }

    /* =========================================================
     * STOP CORE
     * ========================================================= */
    public static void stopCore() {
        if (coreProcess == null || !coreProcess.isAlive()) return;

        try {
            System.out.println("[Runtime] Stopping Core (PID: " + coreProcess.pid() + ")");
            coreProcess.destroy();

            if (!coreProcess.waitFor(5, TimeUnit.SECONDS)) {
                System.out.println("[Runtime] Force stopping Core...");
                coreProcess.destroyForcibly();
                coreProcess.waitFor(3, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            closeStreams();
            coreProcess = null;
        }
    }

    private static void closeStreams() {
        try {
            coreProcess.getInputStream().close();
            coreProcess.getOutputStream().close();
            coreProcess.getErrorStream().close();
        } catch (IOException ignored) {
        }
    }

    /* =========================================================
     * FATAL EXIT (SATU-SATUNYA)
     * ========================================================= */
    private static void fatal(String msg) {
        System.err.println("[Runtime] FATAL: " + msg);
        System.exit(1);
    }

    private static void fatal(String msg, Exception e) {
        System.err.println("[Runtime] FATAL: " + msg);
        e.printStackTrace(System.err);
        System.exit(1);
    }
}
