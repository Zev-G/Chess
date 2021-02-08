package sample.chess.virtual.ai;

import sample.chess.ui.Board;
import sample.chess.virtual.Team;

public abstract class AiBase {


    protected AiBase() {
    }

    public abstract void move(Board board, Team team);

}
