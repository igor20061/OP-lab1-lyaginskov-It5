import java.io.File;
import java.util.Scanner;

public class SystemInfoWin {

    public static void main(String[] args) {
        try {
            // OS Information
            System.out.println("OS: " + System.getProperty("os.name") + " " +
                    System.getProperty("os.version"));

            // Computer and User
            System.out.println("Computer Name: " + getComputerName());
            System.out.println("User: " + System.getProperty("user.name"));

            // Architecture
            System.out.println("Architecture: " + System.getProperty("os.arch"));

            // Memory
            printMemoryInfo();
            printRAMInfo(); // Добавлен вызов информации о RAM

            // Processors
            System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());

            // Drives
            printDriveInfo();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getComputerName() {
        try {
            return System.getenv("COMPUTERNAME");
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private static void printMemoryInfo() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory() / (1024 * 1024); // MB
            long totalMemory = runtime.totalMemory() / (1024 * 1024); // MB
            long freeMemory = runtime.freeMemory() / (1024 * 1024); // MB
            long usedMemory = totalMemory - freeMemory;

            System.out.println("JVM Memory: " + usedMemory + "MB / " + maxMemory + "MB ");

        } catch (Exception e) {
            System.out.println("Memory info unavailable");
        }
    }

    private static void printRAMInfo() {
        try {
            Process process = Runtime.getRuntime().exec("wmic memorychip get capacity");
            Scanner scanner = new Scanner(process.getInputStream());
            long totalRAM = 0;

            // Пропускаем заголовок
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Суммируем объём всех модулей RAM (в байтах)
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    totalRAM += Long.parseLong(line);
                }
            }
            scanner.close();

            // Конвертируем в гигабайты
            long totalRAMGB = totalRAM / (1024 * 1024 * 1024);
            System.out.println("RAM: " + totalRAMGB + " GB");

        } catch (Exception e) {
            System.out.println("RAM info unavailable");
        }
    }

    private static void printDriveInfo() {
        try {
            File[] roots = File.listRoots();
            System.out.println("Drives:");

            for (File root : roots) {
                if (root.exists() && root.getTotalSpace() > 0) {
                    long total = root.getTotalSpace() / (1024 * 1024 * 1024); // GB
                    long free = root.getFreeSpace() / (1024 * 1024 * 1024); // GB
                    long used = total - free;

                    System.out.println("  - " + root.getPath() +
                            " : " + free + " GB free / " + total + " GB total");
                }
            }
        } catch (Exception e) {
            System.out.println("Drive info unavailable");
        }
    }
}
