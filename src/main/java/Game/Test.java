package Game;

import java.util.List;

public class Test {

    public static void main(String[] args) {

        Grid grid = new Grid(7,6);
        grid.play_checker(1,"red");

        grid.play_checker(1,"red");

        String string = grid.toString();
        System.out.println(string);

        grid.toGrid(string);
        grid.display_grid();
        }
}
