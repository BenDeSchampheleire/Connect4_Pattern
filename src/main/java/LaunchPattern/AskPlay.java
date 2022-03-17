package LaunchPattern;

import Game.IGrid;

public class AskPlay implements IAskPlay {

    @Override
    public void askPlay(int column, String color, IGrid grid) {
        grid.play_checker(column,color);
    }
}
