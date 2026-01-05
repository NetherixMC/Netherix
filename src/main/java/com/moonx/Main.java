package com.moonx;

import com.moonx.bootstrap.RuntimeBootstrap;
import com.moonx.gui.MainGUI;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Netherix runtime shutting down...");
            RuntimeBootstrap.shutdown();
        }));
    
        try {
            RuntimeBootstrap.start();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}