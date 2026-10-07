package ca.sheridancollege.project;
import java.io.IOException;
public class Clear {

    private Clear() {
    }

    public static void screen() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            Thread.currentThread().interrupt();
        }
    }
}