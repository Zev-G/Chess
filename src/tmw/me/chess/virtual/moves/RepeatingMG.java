package tmw.me.chess.virtual.moves;

import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;

import java.util.ArrayList;

public class RepeatingMG extends MoveGenerator {

    private final int difX;
    private final int difY;

    public RepeatingMG(VirtualPiece piece, VirtualBoard board, int x, int y) {
        super(piece, board);
        this.difX = x;
        this.difY = y;
    }

    @Override
    public Move[] abstractGenMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int currentX = piece.getX() + difX;
        int currentY = piece.getY() + difY;
        while (currentX >= 0 && currentX <= 7 && currentY >= 0 && currentY <= 7) {
            VirtualPiece pieceAtLocation = board.getPieceAtLocation(currentX, currentY);
            if (pieceAtLocation == null) {
                moves.add(new SimpleMove(piece, currentX, currentY));
                currentX += difX;
                currentY += difY;
            } else if (pieceAtLocation.getTeam() != piece.getTeam()) {
                moves.add(new SimpleMove(piece, currentX, currentY));
                break;
            } else {
                break;
            }
        }
        return moves.toArray(new Move[0]);
    }

    @Override
    public Move[] genThreateningMoves() {
        return abstractGenMoves();
    }

    public int getX() {
        return difX;
    }

    public int getY() {
        return difY;
    }
}
