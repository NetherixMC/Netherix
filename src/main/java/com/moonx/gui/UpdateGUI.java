package com.moonx.gui;

import com.moonx.bootstrap.CoreRuntime;
import com.moonx.progressbar.ProgressBar;
import com.moonx.update.UpdateDownloader;
import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;

import java.util.Scanner;

public class UpdateGUI {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Method untuk cek update dan tampilkan hasilnya
     * Dipanggil dari menu utama
     */
    public static void checkAndShowUpdates() {
        ProgressBar spin = ProgressBar.spinner("\nMengecek update");

        UpdateResult updateResult;

        try {
            // Jalankan spinner sebentar
            for (int i = 0; i < 10; i++) {
                spin.update(0, 0);
                Thread.sleep(100);
            }

            UpdateManager updateManager = new UpdateManager();
            updateResult = updateManager.checkForUpdates();

        } catch (InterruptedException e) {
            spin.finish();
            Thread.currentThread().interrupt();
            System.out.println("\nPemeriksaan update dibatalkan.");
            return;
        }

        spin.finish();

        switch (updateResult.getResult()) {
            case UPDATE_AVAILABLE:
                showUpdateAvailable(updateResult);
                break;

            case UPDATE_NOT_AVAILABLE:
                noUpdatesAvailable(updateResult);
                break;

            case UPDATE_ERROR:
                showUpdateError(updateResult);
                break;
        }
    }

    /**
     * Dialog saat update tersedia
     */
    private static void showUpdateAvailable(UpdateResult updateResult) {
        System.out.println("\n=== PEMBARUAN TERSEDIA ===");
        System.out.println("Versi terbaru : " + updateResult.getVersion());
        System.out.println("Info          : " + updateResult.getMessage());

        while (true) {
            System.out.print("\nPerbarui sekarang? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("ya")) {
                startDownload(updateResult);
                break;
            }

            if (input.equals("n") || input.equals("tidak")) {
                System.out.println("\nPembaruan ditunda.");
                break;
            }

            System.out.println("Input tidak valid.");
        }
    }

    /**
     * Proses download update
     */
    private static void startDownload(UpdateResult updateResult) {
        System.out.println("\nMengunduh pembaruan...");

        ProgressBar bar = ProgressBar.console()
                .message("Download Netherix Core");

        boolean success = UpdateDownloader.download(updateResult, (downloaded, total) -> {
            bar.update(downloaded, total);
        });

        bar.finish();

        if (success) {
            System.out.println("\n✔ Pembaruan berhasil diunduh");
            CoreRuntime.startCore();
        } else {
            System.out.println("\n✖ Gagal mengunduh pembaruan.");
            System.out.println("Silakan coba lagi.");
        }
    }

    /**
     * Tidak ada update
     */
    private static void noUpdatesAvailable(UpdateResult updateResult) {
        System.out.println("\n=== PEMERIKSAAN PEMBARUAN ===");
        System.out.println(updateResult.getMessage());
    }

    /**
     * Error saat cek update
     */
    private static void showUpdateError(UpdateResult updateResult) {
        System.out.println("\n=== ERROR PEMBARUAN ===");
        System.out.println("Pesan: " + updateResult.getMessage());
    }
}