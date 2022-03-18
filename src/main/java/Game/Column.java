package Game;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <h1>Column</h1>
 * Represents a Column, characterized by an identification number and a height. Also contains an Arraylist filled with blank {@link Checker}s.
 * @see Checker
 * @see Grid
 */
public class Column {

    private final int id;
    private final int height;
    private final ArrayList<Checker> column;

    public Column(int id, int height) {

        this.id = id;
        this.height = height;
        this.column = new ArrayList<>(height);

        for (int i = 0; i < this.height; i++) {
            this.column.add( new Checker(i+1,"blank") );
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Checker> getColumn() {
        return column;
    }

    /**
     * Checks is the column if full.
     *
     * @return true/false
     */
    public boolean checkFull() {

        return !Objects.equals(this.getColumn().get(this.height - 1).getColor(), "blank");
    }
}
