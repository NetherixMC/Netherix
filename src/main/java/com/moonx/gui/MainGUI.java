package com.moonx.gui;

import com.moonx.update.UpdateManager;
import com.moonx.bootstrap.RuntimeBootstrap;
import java.util.Scanner;

public class MainGUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void show() {
        boolean running = true;

        while (running) {
            clearScreen();
            System.out.println("\n=== Aplikasi Netherix ===");
            System.out.println("1. Cek Update");
            System.out.println("2. Tampilkan Info");
            System.out.println("3. Software");
            System.out.println("4. Console");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());

                switch (pilihan) {
                    case 1:
                        clearScreen();
                        // Panggil UpdateGUI untuk handle semua logika update
                        UpdateGUI.checkAndShowUpdates();
                        break;
                    case 2:
                        clearScreen();
                        showAppInfo();
                        break;
                    case 3:
                        clearScreen();
                        showSoftwareMenu();
                        break;
                    case 4:
                        clearScreen();
                        showConsole();
                        break;
                    case 5:
                        //clearScreen();
                        System.out.println("DEBUG: Memanggil shutdown...");
                  		scanner.close();
                  		RuntimeBootstrap.shutdown();
                  		return;
                    default:
                        System.out.println("\nPilihan tidak valid!");
                }

                if (pilihan >= 1 && pilihan <= 4) {
                    System.out.println("\nKetik Exit untuk kembali ke menu...");
                    String input = scanner.nextLine();
                    // Loop akan otomatis lanjut
                }

            } catch (NumberFormatException e) {
                System.out.println("\nMasukan tidak valid. Harap masukkan angka!");
                System.out.println("\nTekan Enter untuk melanjutkan...");
                scanner.nextLine();
            }
        }
    }

    private static void showAppInfo() {
        System.out.println("\n=== Informasi Aplikasi ===");
        System.out.println("Netherix v" + UpdateManager.getCurrentVersion());
        System.out.println("\u00A9 2025 Netherix Team");
    }

    private static void showSoftwareMenu() {
        System.out.println("\n=== Software ===");
        System.out.println("Fitur ini akan segera hadir...");
        // lihat list software dari core
    }

    private static void showConsole() {
        System.out.println("\n=== Console ===");
        System.out.println("Coming Soon...");
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Jika clear screen gagal, cetak beberapa baris kosong
            System.out.println("\n".repeat(50));
        }
    }
}