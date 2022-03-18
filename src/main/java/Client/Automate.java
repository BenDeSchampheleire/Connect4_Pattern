package Client;

import Game.Grid;
import javafx.scene.layout.GridPane;

import java.beans.PropertyChangeSupport;

/**
 * <h1>Automate</h1>
 * Represents an Automate, which serves as an intermediate between the Client and the Server.
 */
public class Automate implements IAutomate{

    private String color;
    public ClientTCP myClientTCP;
    private final PropertyChangeSupport notifier;

    public Automate(ClientTCP aClient) {

        this.color = "";
        myClientTCP = aClient;
        notifier = new PropertyChangeSupport(this);
    }


    public void setColor(String color) {this.color = color;}

    public String getColor() {return color;}

    public PropertyChangeSupport getNotifier() {
        return notifier;
    }

    /**
     * Connects to the TCP Server.
     * @see ClientTCP#connectServer()
     * @return true/false
     */
    @Override
    public boolean connectGame() {
        return myClientTCP.connectServer();
    }

    /**
     * Deconnects the TCP Server.
     * @see ClientTCP#deconnectServer()
     */
    @Override
    public void deconnectGame() {
        myClientTCP.deconnectServer();
    }

    /**
     * Sends a request to the TCP Server to play.
     * @param columnNumber number of the column to play
     * @param color color of the Checker
     * @see ClientTCP#transmitCommand(String)
     */
    public void askPlay(int columnNumber, String color, GridPane gridPane) {

        System.out.println("****** Ask to play ********");

        myClientTCP.transmitCommand(color + " " + columnNumber);

        notifier.firePropertyChange("Played",gridPane,null);
    }

    /**
     * Sends a request to the TCP Server to give a color to the Automate.
     * @see ClientTCP#transmitCommand(String)
     */
    public void assignColor() {

        System.out.println("****** Ask color ********");

        String color = myClientTCP.transmitCommand("GiveMeAColor");
        setColor(color);
        System.out.println("Color assigned: " + color);
    }

    /**
     * Sends a request to the TCP Server asking if it's the turn of the Automate.
     * @see ClientTCP#transmitCommand(String)
     * @return true/false
     */
    public boolean askTurn() {

        System.out.println("****** Ask turn ********");

        String color = myClientTCP.transmitCommand("MyTurnToPlay?");

        // true if it is his turn to play, false otherwise
        return color.contains(this.getColor());
    }

    /**
     * Sends a request to the TCP Server to give its Grid.
     * @see ClientTCP#transmitCommand(String)
     * @return Grid object
     */
    public Grid askGrid() {
        // create a new grid (dimensions are unimportant as the String representation will be converted)
        Grid grid = new Grid(0,0);
        System.out.println("****** Ask grid ********");

        String string = myClientTCP.transmitCommand("GiveMeTheGrid");
        grid = grid.toGrid(string);
        return grid;
    }
}
