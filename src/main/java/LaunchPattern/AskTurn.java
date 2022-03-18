package LaunchPattern;

import Game.Grid;
import Game.IGrid;

/**
 * <h1>AskTurn</h1>
 * Using a separate class for a certain command in the framework of the Pattern Strategy.
 * @see "Pattern Strategy"
 */
public class AskTurn implements IAskTurn{

    /**
     * Calls the method {@link Grid#playerTurn()}.
     * @param grid grid that gives the turn
     * @return red or yellow
     */
    @Override
    public String askTurn(IGrid grid) {
        return grid.playerTurn();
    }
}
