package tmw.me.chess.virtual.ai;

import tmw.me.chess.ui.board.Board;
import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.moves.Move;

import java.util.HashMap;

/**
 * Plan on using:
 *  Simple Negamax,
 *  Transposition Tables,
 *    Zobrist Keys,
 *  Alpha Beta Pruning
 */
public abstract class NegaScoutAi extends AiBase {

    protected final int depth;
    protected boolean runOnSeparateThread = true;

    protected double minWaitTime = 150;

    private long moveCounter = 0;

    protected final HashMap<Long, NegamaxPosition> transpositionTable = new HashMap<>();

    protected NegaScoutAi(int depth) {
        super();
        this.depth = depth;
    }

    protected abstract double evaluate();

    @Override
    public void move(VirtualBoard board, Team team, Board visualBoard) {

    }



    private static class NegamaxPosition {

        private Move bestMove;
        private double eval;
        private double alpha;
        private double beta;

    }

}
