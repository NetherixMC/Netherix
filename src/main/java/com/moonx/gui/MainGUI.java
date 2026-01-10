package com.moonx.gui;

import com.moonx.update.UpdateManager;
import com.moonx.bootstrap.RuntimeBootstrap;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class MainGUI {

    private static final Scanner scanner = new Scanner(System.in);
    public static volatile boolean running = true;
    private static volatile boolean inMenu = false;
    private static volatile boolean inputCancelled = false;

    public static void show() {
        while (running) {
            try {
                clearScreen();
                tampilkanMenu();

                inMenu = true;
                inputCancelled = false;

                String input = scanner.nextLine().trim(); // ⬅️ baca input
                inMenu = false;

                if (inputCancelled) {
                    inputCancelled = false;
                    tekanEnter();
                    continue;
                }

                if (input.isEmpty()) continue;

                prosesMenu(Integer.parseInt(input));

            }
            // ================= EOF (FINAL STATE) =================
            catch (NoSuchElementException e) {
                inMenu = false;

                // NoSuchElementException = EOF (PASTI)
                System.err.println("\n[EOF] Input stream ditutup. Runtime dihentikan.");
                RuntimeBootstrap.shutdown();
                System.exit(1);
            }
            // ================= INPUT USER SALAH =================
            catch (NumberFormatException e) {
                inMenu = false;
                System.out.println("\nMasukan tidak valid. Harap masukkan angka 1-5!");
                tekanEnter();
            }
            // ================= ERROR LAIN =================
            catch (Exception e) {
                inMenu = false;
                if (!running) break;
                System.err.println("\nTerjadi kesalahan: " + e.getMessage());
                tekanEnter();
            }
        }
        scanner.close();
    }

    /* ===================== STATE ===================== */

    public static boolean isInMenu() {
        return inMenu;
    }

    public static void cancelInput() {
        inputCancelled = true;
        inMenu = true;
    }

    /* ===================== UI ===================== */

    private static void tampilkanMenu() {
        System.out.println("\n=== Aplikasi Netherix ===");
        System.out.println("1. Cek Update");
        System.out.println("2. Tampilkan Info");
        System.out.println("3. Software");
        System.out.println("4. Console");
        System.out.println("5. Keluar");
        System.out.print("\nPilih menu: ");
    }

    private static void prosesMenu(int pilihan) {
        clearScreen();
        switch (pilihan) {
            case 1:
                UpdateGUI.checkAndShowUpdates();
                tekanEnter();
                break;
            case 2:
                tampilkanInfo();
                tekanEnter();
                break;
            case 3:
                System.out.println("\n=== Software ===");
                System.out.println("Fitur ini akan segera hadir...");
                tekanEnter();
                break;
            case 4:
                System.out.println("\n=== Console ===");
                System.out.println("Coming Soon...");
                tekanEnter();
                break;
            case 5:
                RuntimeBootstrap.shutdown();
                break;
            default:
                System.out.println("\nPilihan tidak valid! Pilih 1-5");
                tekanEnter();
        }
    }

    private static void tampilkanInfo() {
        System.out.println("\n=== Informasi Aplikasi ===");
        System.out.println("Netherix v" + UpdateManager.getCurrentVersion());
        System.out.println("© 2025 Netherix Team");
    }

    /* ===================== INPUT ===================== */

    private static void tekanEnter() {
        System.out.print("\nTekan Enter untuk melanjutkan...");
        try {
            System.in.read();
            while (System.in.available() > 0) System.in.read();
        } catch (Exception ignored) {}
    }

    /* ===================== SCREEN ===================== */

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }
}