package sample.chess.virtual.moves;

import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.VirtualPiece;

public class SimpleMove extends Move {

    public SimpleMove(VirtualPiece piece, int x, int y) {
        super(piece, x, y);
        takePiece = piece.getBoard().getPieceAtLocation(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void abstractUndo(VirtualBoard board) {
        board.movePiece(piece, startX, startY);
        if (takePiece != null) {
            board.addPiece(takePiece, x, y);
        }
    }


    @Override
    protected void abstractDoMove(VirtualBoard board) {
        board.movePiece(piece, x, y);
    }

}
