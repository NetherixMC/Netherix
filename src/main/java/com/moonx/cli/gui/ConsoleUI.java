package com.moonx.cli.gui;

import com.moonx.cli.SoftwareMenuGUI;

public class ConsoleUI {

    /**
     * Tampilkan menu console
     */
    public static void show() {
//        Process process = Downloader.getRunningProcess();
//
//        if (process == null || !process.isAlive()) {
//            System.out.println("⚠️  Tidak ada software yang sedang berjalan.");
//            return;
//        }
//
//        String softwareName = Downloader.getRunningName();
        final String CYAN_BOLD = "\u001B[1;36m";
        final String YELLOW_BOLD = "\u001B[1;33m";
        final String RESET = "\u001B[0m";

        System.out.println("\n==== Console Interface ====");

        System.out.println("Console aktif — ketik 'exit' untuk keluar.\n");

        // Thread untuk membaca output dari software
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("\r" + line); // \r untuk menghapus prompt jika muncul
                    System.out.print("🖥️  " + CYAN_BOLD + softwareName + RESET + " " + YELLOW_BOLD + "/" + RESET); // munculkan prompt ulang
                }
            } catch (IOException e) {
                System.err.println("❌ Gagal membaca output.");
            }
        });
        outputThread.setDaemon(true);
        outputThread.start();

        // Input perintah
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);

        System.out.print("🖥️  " + CYAN_BOLD + softwareName + RESET + " " + YELLOW_BOLD + "/" + RESET);
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            writer.println(input);
            System.out.print("🖥️  " + CYAN_BOLD + softwareName + RESET + " " + YELLOW_BOLD + "/" + RESET);
        }
    }
}