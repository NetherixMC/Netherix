package com.moonx.cli;

import com.moonx.Main;

public class SoftwareMenuGUI {
    /**
     * Tampilkan menu software
     */
    public static void show() {
        System.out.println("\n=== Software ===");
        System.out.println("1. Start");
        System.out.println("2. Stop");
        System.out.println("3. Restart");
        System.out.println("4. Update");
        System.out.println("5. Settings");
        System.out.println("6. Console");
        System.out.println("7. Exit");
        System.out.print("\nSelect menu: ");
    }

    private static void prosesMenu(int pilihan) {
        MainGUI.clearScreen();
        switch (pilihan) {
            case 1:
                System.out.println("Memulai software...");
                //SoftwareStart();
                MainGUI.prosesMenuEnter();
                break;
            case 2:
                System.out.println("Menghentikan software...");
                //SoftwareStop();
                MainGUI.prosesMenuEnter();
                break;
            case 3:
                System.out.println("Merestart software...");
                //SoftwareRestart();
                MainGUI.prosesMenuEnter();
                break;
            case 4:
                System.out.println("Memperbarui software...");
                //SoftwareUpdate();
                MainGUI.prosesMenuEnter();
                break;
            case 5:
                System.out.println("Membuka pengaturan software...");
                //SettingUI.show();
                MainGUI.prosesMenuEnter();
                break;
            case 6:
                System.out.println("Membuka console software...");
                //ConsoleUI.show();
                MainGUI.prosesMenuEnter();
                break;
            case 7:
                MainGUI.show();
                break;
            default:
                System.out.println("\nPilihan tidak valid! Pilih 1-7");
                MainGUI.prosesMenuEnter();
        }
    }
}