package sample.chess.virtual.moves;

import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.VirtualPiece;

import java.util.ArrayList;

public abstract class MoveGenerator {

    protected final VirtualPiece piece;
    protected final VirtualBoard board;

    protected MoveGenerator(VirtualPiece piece, VirtualBoard board) {
        this.piece = piece;
        this.board = board;
    }

    public Move[] genMoves() {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Move move : abstractGenMoves()) {
            if (!board.movePutsInCheck(move)) {
                validMoves.add(move);
            }
        }
        return validMoves.toArray(new Move[0]);
    }
    public Move[] genMovesWithoutCheckingIfLegal() {
        return abstractGenMoves();
    }
    protected abstract Move[] abstractGenMoves();
    public abstract Move[] genThreateningMoves();

    public VirtualBoard getBoard() {
        return board;
    }

    public VirtualPiece getPiece() {
        return piece;
    }
}
