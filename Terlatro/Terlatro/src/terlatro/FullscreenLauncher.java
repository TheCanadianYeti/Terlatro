/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package terlatro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility to relaunch the terminal application in full-screen / maximized mode.
 *
 * @author Marcus Podnar
 * Note: I HAVE NO CLUE HOW THIS CODE WORKS IN THIS CLASS. I DIDNT MAKE IT.
 * FOUND IT ONLINE! NOT CLAIMING IT AS MY OWN OR THAT I MADE IT.
 * NO PLAGIARISM HAPPENDED HERE! - Marcus Podnar
 */
public class FullscreenLauncher {

    /**
     * Checks if the app is running in full screen. If not, launches a new
     * full-screen/maximized terminal window and returns true so the caller can exit.
     *
     * @param args command-line arguments passed to main
     * @param mainClass the main application class to execute
     * @return true if relaunch was triggered (calling main should exit), false otherwise
     */
    public static boolean relaunchIfNeeded(String[] args, Class<?> mainClass) {
        if (args != null && (Arrays.asList(args).contains("--fullscreen") || Arrays.asList(args).contains("--no-fullscreen"))) {
            return false; // Already in full screen or explicitly disabled; proceed with execution
        }

        try {
            File codeLocation = new File(mainClass.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            String path = codeLocation.getAbsolutePath();
            String os = System.getProperty("os.name").toLowerCase();

            // Locate the exact Java executable running this JVM to avoid version mismatches
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
            File javaExe = new File(javaBin + ".exe");
            if (javaExe.exists()) {
                javaBin = javaExe.getAbsolutePath();
            }

            // Automatically handle both packaged .jar files and NetBeans build/classes folders
            String[] javaCmd = path.endsWith(".jar")
                    ? new String[]{javaBin, "-jar", path, "--fullscreen"}
                    : new String[]{javaBin, "-cp", path, mainClass.getName(), "--fullscreen"};

            if (os.contains("win")) {
                launchWindows(javaCmd);
            } else if (os.contains("mac")) {
                launchMac(javaCmd);
            } else if (os.contains("nix") || os.contains("nux")) {
                launchLinux(javaCmd);
            }
            return true; // Relaunch triggered; tell main() to exit this initial window

        } catch (Exception e) {
            // If relaunching fails, fall back to running in the current terminal/console
            System.err.println("Could not auto-fullscreen: " + e.getMessage());
            return false;
        }
    }

    private static void launchWindows(String[] javaCmd) throws IOException {
        try {
            // Try Windows Terminal full screen first if available
            List<String> wtCmd = new ArrayList<>();
            wtCmd.add("wt");
            wtCmd.add("-F");
            wtCmd.addAll(Arrays.asList(javaCmd));
            new ProcessBuilder(wtCmd).start();
        } catch (IOException e) {
            // Fallback to maximized Command Prompt keeping the terminal open with cmd /k
            List<String> fallbackCmd = new ArrayList<>();
            fallbackCmd.add("cmd");
            fallbackCmd.add("/c");
            fallbackCmd.add("start");
            fallbackCmd.add("\"Terlatro\""); // Window title
            fallbackCmd.add("/max");
            fallbackCmd.add("cmd");
            fallbackCmd.add("/k"); // Keeps terminal open so output and interactive game are visible
            fallbackCmd.addAll(Arrays.asList(javaCmd));
            new ProcessBuilder(fallbackCmd).start();
        }
    }

    private static void launchMac(String[] javaCmd) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < javaCmd.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append("'").append(javaCmd[i]).append("'");
        }

        String appleScript = String.format(
            "tell application \"Terminal\"\n" +
            "    activate\n" +
            "    do script \"%s; exit\"\n" +
            "    delay 0.5\n" +
            "    tell application \"System Events\" to keystroke \"f\" using {control down, command down}\n" +
            "end tell", sb.toString());
        new ProcessBuilder("osascript", "-e", appleScript).start();
    }

    private static void launchLinux(String[] javaCmd) throws IOException {
        List<String> linuxCmd = new ArrayList<>();
        linuxCmd.add("gnome-terminal");
        linuxCmd.add("--full-screen");
        linuxCmd.add("--");
        linuxCmd.addAll(Arrays.asList(javaCmd));
        new ProcessBuilder(linuxCmd).start();
    }
}
