package Client;

import Game.Grid;
import javafx.scene.layout.GridPane;

import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class Automate implements IAutomate{

    private String color;
    public ClientTCP myClientTCP;
    private final PropertyChangeSupport notifier;

    public Automate(ClientTCP aClient) {

        this.color = "red";
        myClientTCP = aClient;
        notifier = new PropertyChangeSupport(this);
    }


    public void setColor(String color) {this.color = color;}

    public String getColor() {return color;}

    public PropertyChangeSupport getNotifier() {
        return notifier;
    }

    @Override
    public boolean connectGame() {
        return myClientTCP.connectServer();
    }

    @Override
    public void deconnectGame() {
        myClientTCP.deconnectServer();
    }

    public void askPlay(int columnNumber, String color, GridPane gridPane) {

        System.out.println("****** Ask to play ********");

        myClientTCP.transmitCommand(color + " " + columnNumber);

        notifier.firePropertyChange("Played",gridPane,null);
    }

    public String demandeColor() {

        System.out.println("****** demande color ********");

        String color = myClientTCP.transmitCommand("GiveMeAColor");
        setColor(color);
        System.out.println("Color assigned: " + color);
        return color;
    }

    public boolean askTurn() {

        System.out.println("****** Ask turn ********");

        String color = myClientTCP.transmitCommand("MyTurnToPlay?");

        // true if it is his turn to play, false otherwise
        return Objects.equals(color, this.getColor());
    }

    public Grid askGrid() {
        Grid grid;
        System.out.println("****** Ask grid ********");

        grid = myClientTCP.transmitGrid("GiveMeTheGrid");

        return grid;
    }
}
