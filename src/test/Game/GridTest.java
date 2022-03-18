package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void play_checker() {
        // we create a grid
        Grid g1 = new Grid(7, 6);

        // we add a red checker to the first column
        g1.playChecker(1, "red");
        assertEquals(g1.getGrid().get(0).getColumn().get(0).getPosition(), 1);
        assertEquals(g1.getGrid().get(0).getColumn().get(0).getColor(), "red");

        // we add a yellow checker to the first column (on top of the red one)
        g1.playChecker(1, "yellow");
        assertEquals(g1.getGrid().get(0).getColumn().get(1).getPosition(), 2);
        assertEquals(g1.getGrid().get(0).getColumn().get(1).getColor(), "yellow");

        // we verify that the other spots are still empty ('blank' checkers)
        assertEquals(g1.getGrid().get(0).getColumn().get(5).getColor(), "blank");

    }

    @Test
    void checkWin() {
        // we create a grid
        Grid g1 = new Grid(7, 6);
        // we verify the horizontal win
        g1.playChecker(1,"red");
        g1.playChecker(2,"red");
        g1.playChecker(3,"red");
        g1.playChecker(4,"red");
        assertTrue(g1.checkWin("red"));

        // we create a new grid
        Grid g2 = new Grid(7, 6);
        // we verify the vertical win
        g2.playChecker(1,"yellow");
        g2.playChecker(1,"yellow");
        g2.playChecker(1,"yellow");
        g2.playChecker(1,"yellow");
        assertTrue(g2.checkWin("yellow"));

        // we create a new grid
        Grid g3 = new Grid(7, 6);
        // we verify the /-diagonal win
        g3.playChecker(1,"yellow");
        g3.playChecker(2,"yellow");
        g3.playChecker(2,"yellow");
        g3.playChecker(3,"yellow");
        g3.playChecker(3,"yellow");
        g3.playChecker(3,"yellow");
        g3.playChecker(4,"yellow");
        g3.playChecker(4,"yellow");
        g3.playChecker(4,"yellow");
        g3.playChecker(4,"yellow");
        assertTrue(g3.checkWin("yellow"));

        // we create a new grid
        Grid g4 = new Grid(7, 6);
        // we verify the \-diagonal win
        g4.playChecker(1,"yellow");
        g4.playChecker(1,"yellow");
        g4.playChecker(1,"yellow");
        g4.playChecker(1,"yellow");
        g4.playChecker(2,"yellow");
        g4.playChecker(2,"yellow");
        g4.playChecker(2,"yellow");
        g4.playChecker(3,"yellow");
        g4.playChecker(3,"yellow");
        g4.playChecker(4,"yellow");
        assertTrue(g4.checkWin("yellow"));

        // we create a new grid
        Grid g5 = new Grid(7, 6);
        // we verify using checkers of different colors
        g5.playChecker(1,"yellow");
        g5.playChecker(1,"yellow");
        g5.playChecker(1,"red");
        g5.playChecker(1,"yellow");
        assertFalse(g5.checkWin("yellow"));
        assertFalse(g5.checkWin("red"));

    }
}