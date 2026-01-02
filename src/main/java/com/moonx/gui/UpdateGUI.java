package com.moonx.gui;

import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;
import java.util.Scanner;

public class UpdateGUI {
    private static final Scanner scanner = new Scanner(System.in);

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

    public static void noUpdatesAvailable() {
        System.out.println("\n=== PEMERIKSAAN PEMBARUAN ===");
        System.out.println("Aplikasi Anda sudah menggunakan versi terbaru!");
    }
}