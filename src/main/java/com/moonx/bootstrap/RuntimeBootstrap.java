package com.moonx.bootstrap;

import com.moonx.gui.MainGUI;
import com.moonx.gui.UpdateGUI;
import com.moonx.update.UpdateManager;
import com.moonx.update.UpdateResult;

public class RuntimeBootstrap {
    public static void start() {
        // Check for updates in UpdateManager
        UpdateManager updateManager = new UpdateManager();
        UpdateResult updateResult = updateManager.checkForUpdates();

        // Show main menu
        MainGUI.show();
    }

}