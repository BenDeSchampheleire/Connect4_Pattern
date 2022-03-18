package Game;

import ServerPattern.IContext;

public interface IGrid extends IContext {

    void playChecker(int column_number, String color);

    String playerTurn();

    void startGame();
}
