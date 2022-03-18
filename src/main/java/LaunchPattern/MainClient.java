package LaunchPattern;

import Client.GameApplication;
import javafx.application.Application;

/**
 * <h1>MainClient</h1>
 * Main method for the Client. Launches the {@link GameApplication}.
 */
public class MainClient {

    public static void main(String[] args) {
        // Launches the GUI
        Application.launch(GameApplication.class, args);
    }

}
