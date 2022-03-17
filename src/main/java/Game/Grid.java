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
 * This class represents the grid
 *
 */
public class Grid implements IGrid, Serializable {

    private int width;

    public ServerTCP GameServer;
    private transient IAskPlay StrategyPlay;
    private transient IAskTurn StrategyTurn;

    private int height;
    private ArrayList<Column> grid;

    private String turn;

    /**
     * Creates a grid object
     * A grid has a list of columns
     *
     * @param width: number of columns
     *        height: size of the columns
     *
     */
    public Grid(int width, int height) {

        GameServer = new ServerTCP(this, new Protocol(), 6666);
        //GameServer.setGrid(this);

        this.width = width;
        this.height = height;
        this.grid = new ArrayList<>(width);

        // Initialised to "red" since it is the first team to play
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

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<Column> getGrid() {
        return grid;
    }

    public void setGrid(ArrayList<Column> grid) {
        this.grid = grid;
    }

    public void display_grid() {

        System.out.println("1 2 3 4 5 6 7");
        System.out.println("---------------");

        for (int row = this.height - 1; row >= 0; row--) {
            System.out.print("|");
            for (int col = 0; col < this.width; col++) {
                grid.get(col).getColumn().get(row).display_checker();
                System.out.print("|");
            }
            System.out.println("\n---------------");
        }
        System.out.println("1 2 3 4 5 6 7");
    }

    /**
     * This method puts a checker of a given color in the specified column
     *
     * @param column_number: number of the column to put the checker in
     *        color: checker's color
     *
     */


    public void play_checker(int column_number, String color) {

        Assert.assertTrue(1 <= column_number && column_number <= this.width);

        Column column = this.getGrid().get(column_number - 1);
        Checker checkerSave = null;
        for (int i = 0; i < this.height; i++) {
            Checker checker = column.getColumn().get(i);
            if (checker.getColor().equals("blank")) {
                checker.setColor(color);
                checkerSave = checker;
                break;
            }
        }

        if (Objects.equals(getTurn(), "red")) {
            setTurn("yellow");
        }
        else {
            setTurn("red");
        }

        display_grid();

        // notifie l'interface graphique que la grille a changé
        // on envoie le pion a modifier et sa couleur
        //GameServer.getNotifier().firePropertyChange("iPlayed", checkerSave , color);

    }

    public synchronized void askPlay(int column, String color) {
        StrategyPlay.askPlay(column, color, this);
    }

    public synchronized String askTurn() {
        return StrategyTurn.askTurn(this);
    }

    /**
     * This method checks if the team of the given color has won
     *
     * @param color: color of the team
     *
     * @return true if the team has won
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
     * This method checks if the grid is full
     *
     * @return true is the grid is full
     */
    public boolean boardFull() {

        boolean full = true;
        for (int col = 0; col < this.width; col++) {
            if ( !this.getGrid().get(col).check_full() ) {
                full = false;
            }
        }
        return full;
    }

    /**
     * This method checks the state of the game
     *
     * @return the color of the winner's team
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
     * This method starts the server that hosts the game
     *
     */
    public void startGame() {
        GameServer.go();
    }

    /**
     * This method assigns a color to an automate
     *
     * @return the asigned color
     */
   /* public String assignColor() {
        String color;
        if (this.getNum() % 2 == 0) {
            color = "red";
        }
        else {
            color = "yellow";
        }
        setNum(getNum() + 1);
        return color;
    }*/

    /**
     * This method checks whose turn it is to play
     *
     * @return the color whose turn it is to play
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
}
