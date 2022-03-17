package LaunchPattern;

import Game.Grid;

public class MainServer {

    public static void main(String[] args) {

        Grid grid = new Grid(7, 6, new AskPlay(), new AskTurn());
        System.out.println("Created a grid: " + grid);

        // Start the game
        grid.startGame();
    }

}
