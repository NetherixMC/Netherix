package com.moonx.bootstrap;

import com.moonx.cli.gui.recovery.RecoveryUI;

public class DevModeActivator {
    public static void enableDevMode() {
        if (RecoveryUI.inRecoveryMenu) { // Ganti dengan kondisi aktual untuk mengaktifkan mode DEV
            System.out.println("Mode DEV telah diaktifkan.");
            // Implementasi logika untuk mengaktifkan mode DEV
        } else {
            System.out.println("Gagal mengaktifkan Mode DEV.");
            RuntimeBootstrap.restart();
        }
    }
}