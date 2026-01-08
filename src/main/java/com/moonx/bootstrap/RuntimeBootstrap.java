package com.moonx.bootstrap;

import com.moonx.gui.MainGUI;
import com.moonx.gui.UpdateGUI;
import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class RuntimeBootstrap {
    private static volatile boolean shuttingDown = false;

    public static void start() {
        UpdateGUI.checkAndShowUpdates();
        
        MainGUI.show();
        
    }

    public static void shutdown() {
        if (shuttingDown) return;

        shuttingDown = true;
        MainGUI.running = false;

        System.out.println("\n\nMenghentikan aplikasi...");
        System.out.println("Terima Kasih telah menggunakan Netherix");
        System.exit(0);
    }

    public static void setupShutdownHook() {
        try {
            // Handle Ctrl+C dengan Signal handler
            Signal.handle(new Signal("INT"), new SignalHandler() {
                public void handle(Signal sig) {
                    if (MainGUI.isInMenu()) {
                        // Jangan shutdown jika di menu
                        System.out.println("\n\nCtrl+C terdeteksi! Gunakan menu 5 untuk keluar dengan aman.");
                        MainGUI.cancelInput();
                    } else {
                        // Shutdown jika tidak di menu
                        System.out.println("\n\nMenerima sinyal shutdown (Ctrl+C)...");
                        shutdown();
                    }
                }
            });
        } catch (Exception e) {
            // Fallback ke shutdown hook biasa jika Signal tidak tersedia
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (!shuttingDown) {
                    System.out.println("\nMenerima sinyal shutdown...");
                    shutdown();
                }
            }));
        }
    }
}