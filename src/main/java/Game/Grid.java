package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import LaunchPattern.IAskPlay;
import LaunchPattern.IAskTurn;
import LaunchPattern.Protocol;
import ServerPattern.ServerTCP;
import org.junit.Assert;

/**
 * <h1>Grid</h1>
 * Represents the object Grid, containing: <br>
 * - an ArrayList of ({@link Column}s) <br>
 * - a certain width (number of Columns) and height (size of the Columns) <br>
 * - a TCP Server ({@link ServerTCP}) <br>
 * - different Strategies (Interfaces) <br>
 * - a turn indicator (red or yellow)
 *
 */
public class Grid implements IGrid, Serializable {

    private final int width;
    private final int height;
    private ArrayList<Column> grid;

    public ServerTCP GameServer;
    private transient IAskPlay StrategyPlay;
    private transient IAskTurn StrategyTurn;

    private String turn;


    public Grid(int width, int height) {

        GameServer = new ServerTCP(this, new Protocol(), 6666);

        this.width = width;
        this.height = height;
        this.grid = new ArrayList<>(width);

        // Initialised to "red" since it is the first team to play (choice)
        this.turn = "red";

        for (int i = 0; i < this.width; i++) {
            this.grid.add(new Column(i, this.height));
        }
    }

    public Grid(int width, int height, IAskPlay strategyPlay, IAskTurn strategyTurn) {

        this(width,height);
        this.StrategyPlay = strategyPlay;
        this.StrategyTurn = strategyTurn;
    }

    public String getTurn() {return turn;}

    public void setTurn(String turn) {this.turn = turn;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Column> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<Column> grid) {
        this.grid = grid;
    }

    /**
     * This method allows to transform a Grid into a String representation, which is easily sent from the Server.
     * @return String representation
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int col = 0; col < this.getWidth(); col++) {

            Column column = this.getGrid().get(col);

            for (int row = 0; row < this.getHeight(); row++) {
                Checker checker = column.getColumn().get( this.getHeight() - 1 - row );

                string.append(checker.getColor()).append(",");
            }

            string.append("|");

            }
        return string.toString();
    }

    /**
     * This method counts the number of occurrences of a certain subString in a String.
     * @return number of occurrences
     */
    public int countMatch(String input, String match) {
        int index = input.indexOf(match);
        int count = 0;
        while (index != -1) {
            count++;
            input = input.substring(index + 1);
            index = input.indexOf(match);
        }
        return count;
    }

    /**
     * This method allows to transform the String representation of a Grid into a Grid object.
     * Hence, received Strings from the Server to be transformed in Grid objects.
     * @return Grid object
     */
    public Grid toGrid(String string) {

        int width = countMatch(string,"|");
        int height = countMatch(string,",") / width;

        Grid grid = new Grid(width, height);

        String[] parts = string.split(",");

        for (int col = 0; col < width; col++) {

            Column column = grid.getGrid().get(col);

            for (int row = 0; row < height; row++) {
                Checker checker = column.getColumn().get( grid.getHeight() - 1 - row );

                if (parts[row + height*col].contains("red")) {
                    checker.setColor("red");

                } else if (parts[row + height*col].contains("yellow")) {
                    checker.setColor("yellow");

                }
            }
        }
        return grid;
    }

    /**
     * This method puts a ({@link Checker}) of a given color in the specified ({@link Column}) of the Grid.
     *
     * @param column_number number of the Column to put the Checker in
     * @param color Checker's color
     *
     */
    public void playChecker(int column_number, String color) {

        Assert.assertTrue(1 <= column_number && column_number <= this.width);

        Column column = this.getGrid().get(column_number - 1);
        for (int i = 0; i < this.height; i++) {
            Checker checker = column.getColumn().get(i);
            if (checker.getColor().equals("blank")) {
                checker.setColor(color);
                break;
            }
        }

        if (Objects.equals(getTurn(), "red")) {
            setTurn("yellow");
        }
        else {
            setTurn("red");
        }
    }

    /**
     * Links with the method {@link IAskPlay#askPlay(int, String, IGrid)} from the Interface {@link IAskPlay}.
     * @see "Pattern Strategy"
     */
    public synchronized void askPlay(int column, String color) {
        StrategyPlay.askPlay(column, color, this);
    }

    /**
     * Links with the method {@link IAskTurn#askTurn(IGrid)} from the Interface {@link IAskTurn}.
     * @see "Pattern Strategy"
     */
    public synchronized String askTurn() {
        return StrategyTurn.askTurn(this);
    }

    /**
     * This method checks if the player of the given color has won on a Grid.
     * @param color color of the player
     * @return true/false
     */
    public boolean checkWin(String color) {

        // verticalCheck
        for (int j = 0; j < this.height - 3; j++) {
            for (int i = 0; i < this.width; i++) {
                if (grid.get(i).getColumn().get(j).getColor().equals(color)
                        && grid.get(i).getColumn().get(j + 1).getColor().equals(color)
                        && grid.get(i).getColumn().get(j + 2).getColor().equals(color)
                        && grid.get(i).getColumn().get(j + 3).getColor().equals(color)) {
                    return true;
                }
            }
        }

        // horizontalCheck
        for (int j = 0; j < this.height; j++) {
            for (int i = 0; i < this.width - 3; i++) {
                if (grid.get(i).getColumn().get(j).getColor().equals(color)
                        && grid.get(i + 1).getColumn().get(j).getColor().equals(color)
                        && grid.get(i + 2).getColumn().get(j).getColor().equals(color)
                        && grid.get(i + 3).getColumn().get(j).getColor().equals(color)) {
                    return true;
                }
            }
        }

        // \-diagonalCheck
        for (int i = 3; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (grid.get(i).getColumn().get(j).getColor().equals(color)
                        && grid.get(i - 1).getColumn().get(j + 1).getColor().equals(color)
                        && grid.get(i - 2).getColumn().get(j + 2).getColor().equals(color)
                        && grid.get(i - 3).getColumn().get(j + 3).getColor().equals(color)) {
                    return true;
                }
            }
        }

        // /-diagonalCheck
        for (int i = 3; i < this.width; i++) {
            for (int j = 3; j < this.height; j++) {
                if (grid.get(i).getColumn().get(j).getColor().equals(color)
                        && grid.get(i - 1).getColumn().get(j - 1).getColor().equals(color)
                        && grid.get(i - 2).getColumn().get(j - 2).getColor().equals(color)
                        && grid.get(i - 3).getColumn().get(j - 3).getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method checks if the grid is full.
     * @return true/false
     */
    public boolean boardFull() {

        boolean full = true;
        for (int col = 0; col < this.width; col++) {
            if ( !this.getGrid().get(col).checkFull() ) {
                full = false;
            }
        }
        return full;
    }

    /**
     * This method checks if a player has won.
     * @return red, yellow, nobody or ongoing
     */
    public String EndOfGame() {

        String winner;
        if ( checkWin("red") ) {
            winner = "red";
        } else if ( checkWin("yellow") ) {
            winner = "yellow";
        } else if ( boardFull() ) {
            winner = "nobody";
        } else {
            winner = "ongoing";
        }
        return winner;
    }

    /**
     * This method starts the TCP Server that hosts the game.
     */
    public void startGame() {
        GameServer.go();
    }

    /**
     * This method checks whose turn it is to play.
     * @return red or yellow
     */
    public String playerTurn() {
        String color;
        if (Objects.equals(this.getTurn(), "red")) {
            color = "red";
        }
        else {
            color = "yellow";
        }
        return color;
    }

    /**
     * This method gives a color to an Automate ({@link Client.Automate}).
     * @return red or yellow
     */
    public String giveColor() {
        String color = this.turn;
        if (color.equals("red")) {
            this.turn = "yellow";
        } else if (color.equals("yellow")) {
            this.turn = "red";
        }
        return color;
    }
}
