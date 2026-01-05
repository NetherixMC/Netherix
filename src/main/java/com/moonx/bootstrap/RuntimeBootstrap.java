package com.moonx.bootstrap;

import com.moonx.gui.MainGUI;
import com.moonx.gui.UpdateGUI;
import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;
import java.util.Scanner;

public class RuntimeBootstrap {
    public static void start() {
        // Check for updates saat startup
        UpdateManager updateManager = new UpdateManager();
        UpdateResult updateResult = updateManager.checkForUpdates();
        
        // Tampilkan hasil pengecekan update
        if (updateResult.getResult() == UpdateResult.Result.UPDATE_AVAILABLE) {
            UpdateGUI.showUpdateAvailable(updateResult);
        }
        
        // Lanjut ke main menu
        MainGUI.show();
    }
    
    public static void shutdown(){
        System.out.println("\nTerima Kasih telah menggunakan Netherix");
        System.exit(0);
    }
}