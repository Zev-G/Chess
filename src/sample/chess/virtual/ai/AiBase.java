package sample.chess.virtual.ai;

import sample.chess.ui.board.Board;
import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;

public abstract class AiBase {


    protected AiBase() {
    }

    public abstract void move(VirtualBoard board, Team team, Board visualBoard);

}
