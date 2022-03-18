package LaunchPattern;

import Game.Grid;

/**
 * <h1>MainServer</h1>
 * Main method for the Server. Creates a grid and calls its {@link Grid#startGame() startGame} method.
 */
public class MainServer {

    public static void main(String[] args) {

        Grid grid = new Grid(7, 6, new AskPlay(), new AskTurn());
        System.out.println("Created a grid");

        // Start the game
        grid.startGame();
    }

}
