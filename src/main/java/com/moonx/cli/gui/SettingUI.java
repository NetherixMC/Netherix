package com.moonx.cli.gui;

import com.moonx.cli.MainGUI;

public class SettingUI {

    /**
     * Tampilkan menu pengaturan
     */
    public static void show() {
        System.out.println("\n=== Settings ===");
        System.out.println("1. Change Language");
        System.out.println("2. Set Theme");
        System.out.println("3. Manage Notifications");
        System.out.println("4. Exit");
        System.out.print("\nSelect menu: ");
    }
}