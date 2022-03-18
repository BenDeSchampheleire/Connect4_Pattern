package Game;

/**
 * <h1>Checker</h1>
 * Represents a Checker, characterized by a color and a position in a certain {@link Column}.
 * @see Column
 * @see Grid
 */
public class Checker {

    private final int position;
    private String color;

    public Checker(int position, String color) {

        this.position = position;
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}