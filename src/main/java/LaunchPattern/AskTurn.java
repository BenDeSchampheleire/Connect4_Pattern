package LaunchPattern;

import Game.IGrid;

public class AskTurn implements IAskTurn{

    @Override
    public String askTurn(IGrid grid) {
        return grid.playerTurn();
    }
}
