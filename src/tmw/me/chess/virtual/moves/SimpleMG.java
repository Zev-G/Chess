package tmw.me.chess.virtual.moves;

import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;

public class SimpleMG extends MoveGenerator {

    private final int xDif;
    private final int yDif;

    private final boolean ableToTake;
    private final boolean requiredToTake;

    public SimpleMG(VirtualPiece piece, VirtualBoard board, int xDif, int yDif, boolean canTake, boolean mustTake) {
        super(piece, board);
        this.xDif = xDif;
        this.yDif = yDif;
        this.ableToTake = canTake;
        this.requiredToTake = mustTake;
    }
    public SimpleMG(VirtualPiece piece, VirtualBoard board, int xDif, int yDif) {
        this(piece, board, xDif, yDif, true, false);
    }

    /**
     *
     * @return The generated moves.
     */
    @Override
    public Move[] abstractGenMoves() {
        int newX = piece.getX() + xDif;
        int newY = piece.getY() + yDif;
        if (newX < 0 || newY < 0 || newX > 7 || newY > 7) {
            return new Move[]{};
        }
        if (board.isPieceAtLocation(newX, newY)) {
            if (ableToTake && board.getPieceAtLocation(newX, newY).getTeam() != piece.getTeam()) {
                return new Move[]{ new SimpleMove(piece, newX, newY) };
            } else {
                return new Move[]{};
            }
        } else if (!requiredToTake) {
            return new Move[]{ new SimpleMove(piece, newX, newY) };
        } else {
            return new Move[]{};
        }
    }

    @Override
    public Move[] genThreateningMoves() {
        return ableToTake ? abstractGenMoves() : new Move[0];
    }

    public boolean isAbleToTake() {
        return ableToTake;
    }
    public boolean isRequiredToTake() {
        return requiredToTake;
    }
}
