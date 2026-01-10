package com.moonx.cli.gui;

import com.moonx.cli.MainGUI;

public class SoftwareUI {

    /**
     * Tampilkan menu software
     */
    public static void show() {
        System.out.println("\n=== Software ===");
        System.out.println("1.Netherix-Bukkit");
        System.out.println("2.Netherix-Proxy");
        System.out.println("3.Netherix-Forge");
        System.out.println("4.Netherix-Fabric");
        System.out.println("5.Netherix-Neoforge");
    }

    private static void prosesMenu(int pilihan) {
        /* Implementasi download software berdasarkan pilihan */
        switch (pilihan) {
            case 1:
                
                break;
            case 2:
                System.out.println("Memulai download Netherix-Proxy...");
                break;
            case 3:
                System.out.println("Memulai download Netherix-Forge...");
                break;
            case 4:
                System.out.println("Memulai download Netherix-Fabric...");
                break;
            case 5:
                System.out.println("Memulai download Netherix-Neoforge...");
                break;
            default:
                System.out.println("\nPilihan tidak valid! Pilih 1-5");
                MainGUI.prosesMenuEnter();
        }
    }
}