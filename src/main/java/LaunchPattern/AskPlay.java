package LaunchPattern;

import Game.IGrid;

/**
 * <h1>AskPlay</h1>
 * Using a separate class for a certain command in the framework of the Pattern Strategy.
 * @see "Pattern Strategy"
 */
public class AskPlay implements IAskPlay {

    /**
     * Calls the method {@link Game.Grid#playChecker(int, String)}.
     * @param column number of the Column
     * @param color color of the Checker
     * @param grid grid on which to play
     */
    @Override
    public void askPlay(int column, String color, IGrid grid) {
        grid.playChecker(column,color);
    }
}
