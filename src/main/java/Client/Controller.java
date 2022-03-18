package Client;

import Game.Checker;
import Game.Column;
import Game.Grid;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

/**
 * This class represents the graphical interface
 * And handles the unfolding of the game
 *
 */
public class Controller implements PropertyChangeListener {

    public Automate automate;
    private Grid grid;
    boolean update;

    public Controller() {

        ClientTCP clientTCP = new ClientTCP("localhost", 6666);

        automate = new Automate(clientTCP);

        // listen to the Server
        automate.connectGame();
        this.grid = automate.askGrid(); // get the grid from the Server
        automate.setColor( this.grid.GameServer.giveColor() );
        System.out.println("Automate color: " + this.grid.GameServer.giveColor());
        automate.deconnectGame();

        if (this.grid == null) {
            System.out.println("Launch the Server");
        }

        automate.getNotifier().addPropertyChangeListener(this);

        this.update = false;

    }

    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonQuit;

    @FXML
    protected void quitWindow() {

        Stage stage = (Stage)this.buttonQuit.getScene().getWindow();
        stage.close();
    }

    public void changeScene() {

        Scene scene = this.buttonQuit.getScene();
        Window window = scene.getWindow();
        Stage stage = (Stage) window;

        drawBoard(stage, this.grid);
        window.centerOnScreen();
    }

    /**
     *
     * @param scene: scene where the screen is displayed
     *        winner: color of the winner's team
     *
     */
    public void winScreen(Scene scene, String winner) throws IOException {

        final VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);

        Label congrats = new Label("Congratulations");
        congrats.setFont(new Font("Arial",48));
        vbox.getChildren().add(congrats);

        Label playerName = new Label();
        playerName.setFont(new Font("Arial",48));
        vbox.getChildren().add(playerName);

        scene.setRoot(vbox);


        switch (winner) {
            case "red" -> {
                playerName.setText("Red wins !");
                playerName.setTextFill(Color.RED);
            }
            case "yellow" -> {
                playerName.setText("Yellow wins !");
                playerName.setTextFill(Color.YELLOW);
            }
            case "nobody" -> playerName.setText("It's a draw !");
        }

    }

    /**
     * This method draws the board on which the game takes place
     *
     * @param stage: stage on which the board is displayed
     *        grid: the grid object, used to know the number of rows and columns to draw
     *
     */
    public void drawBoard(Stage stage, Grid grid) {

        final BorderPane borderPane = new BorderPane();
        final GridPane gridPane = new GridPane();
        stage.setTitle("Connect4");
        stage.setResizable(true);

        Scene scene = new Scene(borderPane, (grid.getWidth()+1)*100, (grid.getHeight()+2)*100, true);
        scene.setFill(Color.TRANSPARENT);

        gridPane.setAlignment(Pos.CENTER);

        // creation of columns
        for (int i = 0; i < grid.getWidth(); i++) {
            gridPane.getColumnConstraints().addAll(new ColumnConstraints(100,100, Double.MAX_VALUE));
        }

        // creation of rows
        for (int j = 0; j < grid.getHeight(); j++) {
            gridPane.getRowConstraints().addAll(new RowConstraints(100,100, Double.MAX_VALUE));
        }

        // creation of the grid
        drawGrid(gridPane, grid);
        borderPane.setCenter(gridPane);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * This method handles the unfolding of the game and the behavior of the grid's cases
     *
     * @param gridPane: the visual grid
     *        grid: the grid object
     *
     */
    private void drawGrid(final GridPane gridPane, Grid grid){

        gridPane.getChildren().clear();

        // creation of the visual grid
        for (int col = 0; col < grid.getWidth(); col++) {

            Column column = grid.getGrid().get(col);

            for (int row = 0; row < grid.getHeight(); row++) {

                Checker checker = column.getColumn().get( grid.getHeight() - 1 - row );

                // create the blue cell
                Rectangle rect = new Rectangle(100, 100);
                Circle circ = new Circle(45);
                circ.centerXProperty().set(50);
                circ.centerYProperty().set(50);
                Shape cell = Path.subtract(rect, circ);
                cell.setFill(Color.BLUE);
                cell.setStroke(Color.BLUE);
                cell.setOpacity(0.8);

                // preview checkers
                final Circle checkerPreview = new Circle(40);
                checkerPreview.setOpacity(0.5);
                checkerPreview.setFill(Color.TRANSPARENT);

                // displays a red or yellow preview checker whenever the player puts the mouse on a playable cell
                checkerPreview.setOnMouseEntered(arg0 -> {

                    // connect to the game
                    automate.connectGame();

                    // if it is his turn to play
                    boolean myTurn = automate.askTurn();
                    automate.deconnectGame();
                    if (myTurn) {
                        System.out.println("your turn");
                        if (Objects.equals(automate.getColor(), "red")) {
                            checkerPreview.setFill(Color.RED);
                        } else {
                            checkerPreview.setFill(Color.YELLOW);
                        }
                    }

                });

                // removes the preview checker whenever the player takes the mouse off the cell
                checkerPreview.setOnMouseExited(arg0 -> checkerPreview.setFill(Color.TRANSPARENT));


                checkerPreview.setOnMouseClicked(arg0 -> {
                    // connect to the game
                    automate.connectGame();
                    // if it is his turn to play
                    boolean myTurn;
                    myTurn = automate.askTurn();
                    automate.deconnectGame();
                    if (myTurn) {
                        automate.connectGame();
                        automate.askPlay(column.getId(), automate.getColor(),gridPane);
                    }
                    else {
                        System.out.println("It is not your turn to play");
                    }
                    automate.deconnectGame();

                    automate.connectGame();
                    this.grid = automate.askGrid();
                    automate.deconnectGame();

                    // drawGrid(gridPane,this.grid);

                    switch ( this.grid.EndOfGame() ) {
                        case "red" -> {
                            try {
                                winScreen(gridPane.getScene(),"red");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case "yellow" -> {
                            try {
                                winScreen(gridPane.getScene(),"yellow");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case "nobody" -> {
                            try {
                                winScreen(gridPane.getScene(),"nobody");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        case "ongoing" -> {}
                    }
                });

                StackPane stack = new StackPane();

                final Circle checker_circle = new Circle(40);
                switch (checker.getColor()) {
                    case "red" -> checker_circle.setFill(Color.RED);
                    case "yellow" -> checker_circle.setFill(Color.YELLOW);
                    case "blank" -> {
                        checker_circle.setFill(Color.TRANSPARENT);
                        checker_circle.setTranslateY(-100 * (row + 1));
                    }
                }

                stack.getChildren().addAll(cell, checkerPreview, checker_circle);

                gridPane.add(stack, col, row);

            }

        }

    }

    /**
     * Cette méthode est appelée quand l'objet Observable fait appel à notifyObservers()
     *
     * Quand le serveur a joué il notifie les interfaces graphiques pour qu'elles se mettent à jour
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName() == "Played") {
            automate.connectGame();
            this.grid = automate.askGrid();
            automate.deconnectGame();

            GridPane gridPane = (GridPane) evt.getOldValue();
            drawGrid(gridPane,this.grid);
        }

    }
}


