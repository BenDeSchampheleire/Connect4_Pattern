package Game;

import java.io.Serializable;

/**
 * This class represents the checkers
 *
 */
public class Checker implements Serializable {


    private int position;
    private String color;

    /**
     * Creates a checker object.
     *
     * @param position: index of the checker in the column
     * @param color: color of the checker
     *
     */
    public Checker(int position, String color) {

        this.position = position;
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void display_checker() {
        if (this.getColor().equals("yellow")) {
            System.out.print("y");
        }
        else if (this.getColor().equals("red")) {
            System.out.print("r");
        }
        else {
            System.out.print(".");
        }
    }
}