package com.moonx.cli;

import com.moonx.update.UpdateManager;
import com.moonx.bootstrap.RuntimeBootstrap;
import com.moonx.cli.gui.UpdateUI;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class MainGUI {

    private static final Scanner scanner = new Scanner(System.in);
    public static volatile boolean running = true;
    public static volatile boolean inMenu = false;
    public static volatile boolean inputCancelled = false;

    public static void show() {
        while (running) {
            try {
                clearScreen();
                tampilkanMenu();

                inMenu = true;
                inputCancelled = false;

                String input = scanner.nextLine().trim();
                inMenu = false;

                if (inputCancelled) {
                    inputCancelled = false;
                    prosesMenuEnter();
                    continue;
                }

                if (input.isEmpty()) continue;

                prosesMenu(Integer.parseInt(input));

            } catch (NoSuchElementException e) {
                handleEOF(e);                     // ← handler khusus EOF
            } catch (NumberFormatException e) {
                handleUserInputError(e);          // ← handler input salah
            } catch (Exception e) {
                handleOtherError(e);              // ← handler error lain
            }
        }
    scanner.close();
    }

    /* ===================== EOF ERROR HANDLERS ===================== */
    private static void handleEOF(NoSuchElementException e) {
        inMenu = false;
        System.err.println("\n[EOF] Input stream ditutup. Runtime dihentikan.");
        RuntimeBootstrap.shutdown();
        System.exit(1);
    }

    /* ===================== INPUT ERROR HANDLER ===================== */

    private static void handleUserInputError(NumberFormatException e) {
        inMenu = false;
        System.out.println("\nMasukan tidak valid. Harap masukkan angka 1-5!");
        prosesMenuEnter();
    }

    /* ===================== OTHER ERROR HANDLER ===================== */

    private static void handleOtherError(Exception e) {
        inMenu = false;
        if (!running) return;
        System.err.println("\nTerjadi kesalahan: " + e.getMessage());
        prosesMenuEnter();
    }


    /* ===================== STATE ===================== */

    public static boolean isInMenu() {
        return inMenu;
    }

    public static void cancelInput() {
        inputCancelled = true;
        inMenu = false;
    }

    /* ===================== UI ===================== */

    private static void tampilkanMenu() {
        System.out.println("\n=== Netherix Software ===");
        System.out.println("1. Check Update");
        System.out.println("2. Show Info");
        System.out.println("3. Software");
        System.out.println("4. Settings");
        System.out.println("5. Exit");
        System.out.print("\nSelect menu: ");
    }

    private static void prosesMenu(int pilihan) {
        clearScreen();
        switch (pilihan) {
            case 1:
                UpdateGUI.checkAndShowUpdates();
                prosesMenuEnter();
                break;
            case 2:
                tampilkanInfo();
                prosesMenuEnter();
                break;
            case 3:
                SoftwareGUI.show();
                prosesMenuEnter();
                break;
            case 4:
                prosesMenuEnter();
                break;
            case 5:
                RuntimeBootstrap.shutdown();
                break;
            default:
                System.out.println("\nPilihan tidak valid! Pilih 1-5");
                prosesMenuEnter();
        }
    }

    private static void tampilkanInfo() {
        System.out.println("\n=== Informasi Aplikasi ===");
        System.out.println("Netherix v" + UpdateManager.getCurrentVersion());
        System.out.println("© 2025 Netherix Team");
    }

    /* ===================== INPUT ===================== */

    public static void prosesMenuEnter() {
        System.out.print("\nTekan Enter untuk melanjutkan...");
        try {
            System.in.read();
            while (System.in.available() > 0) System.in.read();
        } catch (Exception ignored) {}
    }

    /* ===================== SCREEN ===================== */

    public static void clearScreen() {
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