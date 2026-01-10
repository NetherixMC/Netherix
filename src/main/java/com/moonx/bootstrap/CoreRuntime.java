package com.moonx.bootstrap;

import com.moonx.bootstrap.RuntimeBootstrap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoreRuntime {

    public static final String CORE_FILE = "netherix-core.jar";
    private static Process coreProcess;

    public static void startCore() throws IOException {

        // ✅ CEK CORE ADA ATAU TIDAK
        File coreJar = new File(CORE_FILE);
        if (!coreJar.exists() || !coreJar.isFile()) {
            System.err.println("[Runtime] ERROR: netherix-core.jar tidak ditemukan!");
            System.err.println("[Runtime] Runtime dihentikan untuk mencegah sistem tidak konsisten.");
            System.exit(1); // ❌ HARD EXIT
        }

        long runtimePid = ProcessHandle.current().pid();

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add("netherix-core.jar");
        command.add("--parent-pid=" + runtimePid);

        ProcessBuilder pb = new ProcessBuilder(command);

        // ❗ PENTING: jangan detach
        pb.redirectErrorStream(true);

        coreProcess = pb.start();

        System.out.println("[Runtime] Core started (PID: " + coreProcess.pid() + ")");

        // Shutdown hook → pastikan Core mati
        RuntimeBootstrap.setupShutdownHook(new Thread(() -> {
            stopCore();
        }));
    }

    public static void stopCore() {
        if (coreProcess != null && coreProcess.isAlive()) {
            System.out.println("[Runtime] Stopping Core...");
            coreProcess.destroy();

            try {
                coreProcess.waitFor();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
