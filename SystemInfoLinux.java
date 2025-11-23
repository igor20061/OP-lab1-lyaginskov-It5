import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SystemInfoLinux {

    public static void main(String[] args) {
        try {
            System.out.println("=== System Information Linux ===");

            // OS and Kernel info
            printOSInfo();
            printKernelInfo();

            // System info
            printHostname();
            printUserInfo();

            // Memory info
            printMemoryInfo();

            // CPU info
            printCPUInfo();

            // Disk info
            printDiskInfo();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printOSInfo() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("/etc/os-release"));
            for (String line : lines) {
                if (line.startsWith("PRETTY_NAME=")) {
                    String osName = line.split("=")[1].replace("\"", "");
                    System.out.println("OS: " + osName);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("OS: Unknown Linux Distribution");
        }
    }

    private static void printKernelInfo() {
        try {
            Process process = Runtime.getRuntime().exec("uname -sr");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String kernel = reader.readLine();
            System.out.println("Kernel: " + kernel);

            process = Runtime.getRuntime().exec("uname -m");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String arch = reader.readLine();
            System.out.println("Architecture: " + arch);
        } catch (Exception e) {
            System.out.println("Kernel/Architecture: Unknown");
        }
    }

    private static void printHostname() {
        try {
            Process process = Runtime.getRuntime().exec("hostname");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String hostname = reader.readLine();
            System.out.println("Hostname: " + hostname);
        } catch (Exception e) {
            System.out.println("Hostname: Unknown");
        }
    }

    private static void printUserInfo() {
        System.out.println("User: " + System.getProperty("user.name"));
    }

    private static void printMemoryInfo() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("/proc/meminfo"));
            long memTotal = 0, memFree = 0, memAvailable = 0, swapTotal = 0, swapFree = 0;

            for (String line : lines) {
                if (line.startsWith("MemTotal:")) {
                    memTotal = Long.parseLong(line.split("\\s+")[1]) / 1024;
                } else if (line.startsWith("MemFree:")) {
                    memFree = Long.parseLong(line.split("\\s+")[1]) / 1024;
                } else if (line.startsWith("MemAvailable:")) {
                    memAvailable = Long.parseLong(line.split("\\s+")[1]) / 1024;
                } else if (line.startsWith("SwapTotal:")) {
                    swapTotal = Long.parseLong(line.split("\\s+")[1]) / 1024;
                } else if (line.startsWith("SwapFree:")) {
                    swapFree = Long.parseLong(line.split("\\s+")[1]) / 1024;
                }
            }

            System.out.println("RAM: " + memAvailable + "MB free / " + memTotal + "MB total");
            System.out.println("Swap: " + swapTotal + "MB total / " + swapFree + "MB free");

        } catch (Exception e) {
            System.out.println("Memory info: Unavailable");
        }
    }

    private static void printCPUInfo() {
        try {
            // Number of processors
            int processors = Runtime.getRuntime().availableProcessors();
            System.out.println("Processors: " + processors);

            // Load average
            Process process = Runtime.getRuntime().exec("cat /proc/loadavg");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String loadAvg = reader.readLine();
            if (loadAvg != null) {
                String[] loads = loadAvg.split(" ");
                System.out.println("Load average: " + loads[0] + ", " + loads[1] + ", " + loads[2]);
            }

        } catch (Exception e) {
            System.out.println("CPU info: Unavailable");
        }
    }

    private static void printDiskInfo() {
        try {
            System.out.println("Drives:");

            File[] roots = File.listRoots();
            for (File root : roots) {
                if (root.exists() && root.getTotalSpace() > 0) {
                    long totalGB = root.getTotalSpace() / (1024 * 1024 * 1024);
                    long freeGB = root.getFreeSpace() / (1024 * 1024 * 1024);
                    long usedGB = totalGB - freeGB;

                    System.out.println("  " + root.getPath() + " : " +
                            freeGB + "GB free / " + totalGB + "GB total");
                }
            }

        } catch (Exception e) {
            System.out.println("Drive info: Unavailable");
        }
    }
}