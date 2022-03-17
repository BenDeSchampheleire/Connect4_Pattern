package Client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class launches the game
 *
 */
public class GameApplication extends Application {

    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/Welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 369);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
