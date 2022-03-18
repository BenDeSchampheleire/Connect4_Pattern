package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

    @Test
    void check_full() {
        // we create a grid
        Grid g1 = new Grid(7, 6);

        // we verify that no column is full
        for (int i=0; i < g1.getWidth(); i++) {
            assertFalse(g1.getGrid().get(i).checkFull());
        }

        // we fill one column
        g1.playChecker(1, "red");
        g1.playChecker(1, "yellow");
        g1.playChecker(1, "red");
        g1.playChecker(1, "yellow");
        g1.playChecker(1, "red");
        g1.playChecker(1, "yellow");

        // we check if it's full
        assertTrue(g1.getGrid().get(0).checkFull());

    }
}