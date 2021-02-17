package tmw.me.chess.virtual.ai;

import tmw.me.chess.ui.board.Board;
import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;

public abstract class AiBase {


    protected AiBase() {
    }

    public abstract void move(VirtualBoard board, Team team, Board visualBoard);

}
