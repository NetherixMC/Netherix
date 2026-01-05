package com.moonx.gui;

import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;
import java.util.Scanner;

public class UpdateGUI {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Method untuk cek update dan tampilkan hasilnya
     * Digunakan dari menu utama
     */
    public static void checkAndShowUpdates() {
        System.out.println("\nMengecek update...");
        UpdateManager updateManager = new UpdateManager();
        UpdateResult updateResult = updateManager.checkForUpdates();

        switch (updateResult.getResult()) {
            case UPDATE_AVAILABLE:
                showUpdateAvailable(updateResult);
                break;
            case UPDATE_NOT_AVAILABLE:
                noUpdatesAvailable();
                break;
            case UPDATE_ERROR:
                showUpdateError(updateResult);
                break;
        }
    }

    /**
     * Tampilkan dialog update tersedia
     */
    public static void showUpdateAvailable(UpdateResult updateResult) {
        System.out.println("\n=== PEMBARUAN TERSEDIA ===");
        System.out.println("Versi baru tersedia: " + updateResult.getVersion());
        System.out.println("Pesan: " + updateResult.getMessage());

        while (true) {
            System.out.print("\nApakah Anda ingin memperbarui sekarang? (y/n): ");
            String jawaban = scanner.nextLine().trim().toLowerCase();

            if (jawaban.equals("y") || jawaban.equals("ya")) {
                System.out.println("\nMengunduh pembaruan...");
                boolean success = UpdateManager.downloadUpdate(updateResult.getVersion());

                if (success) {
                    System.out.println("Pembaruan berhasil diunduh ke folder 'downloads'");
                    System.out.println("Silakan tutup aplikasi dan jalankan versi terbaru.");
                } else {
                    System.out.println("Gagal mengunduh pembaruan. Silakan coba lagi nanti.");
                }
                break;
            } else if (jawaban.equals("n") || jawaban.equals("tidak")) {
                System.out.println("\nPembaruan akan ditunda.");
                break;
            } else {
                System.out.println("Pilihan tidak valid. Silakan jawab 'y' untuk Ya atau 'n' untuk Tidak.");
            }
        }
    }

    /**
     * Tampilkan pesan tidak ada update
     */
    public static void noUpdatesAvailable() {
        System.out.println("\n=== PEMERIKSAAN PEMBARUAN ===");
        System.out.println("Aplikasi Anda sudah menggunakan versi terbaru!");
    }

    /**
     * Tampilkan error saat cek update
     */
    public static void showUpdateError(UpdateResult updateResult) {
        System.out.println("\n=== ERROR PEMERIKSAAN PEMBARUAN ===");
        System.out.println("Terjadi kesalahan: " + updateResult.getMessage());
    }
}