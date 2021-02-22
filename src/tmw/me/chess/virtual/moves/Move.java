package tmw.me.chess.virtual.moves;

import tmw.me.chess.ui.board.Board;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.extra.Coordinates;

public abstract class Move {

    protected final VirtualPiece piece;

    protected final int x;
    protected final int y;

    protected final int startX;
    protected final int startY;

    protected boolean connectedToOnePiece = true;

    protected VirtualPiece takePiece;
    protected Coordinates previousEnPassant;

    protected Move(VirtualPiece piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.startX = piece.getX();
        this.startY = piece.getY();
    }

    public void doMove(Board board, boolean forReal) {
        doMove(board.getVirtualBoard(), forReal, false);
        if (forReal) {
            board.updateToBoard();
            board.getVirtualBoard().getGame().nextTurn(this);
        }
    }
    public void doMove(VirtualBoard board, boolean forReal) {
        doMove(board, forReal, true);
    }
    public void justMove(VirtualBoard board) {
        abstractDoMove(board);
    }
    public void doMove(VirtualBoard board, boolean forReal, boolean nextTurn) {
        previousEnPassant = board.getGame().getEnPassant();
        piece.moved();
        abstractDoMove(board);
        if (nextTurn && forReal) {
            board.getGame().nextTurn(this);
        }
        if (previousEnPassant != null) {
            Coordinates postEnPassant = board.getGame().getEnPassant();
            if (previousEnPassant.equals(postEnPassant)) {
                board.getGame().setEnPassant(null);
            }
        }
    }
    protected abstract void abstractDoMove(VirtualBoard board);

    public Coordinates getLoc() { return new Coordinates(x, y); }
    public Coordinates getStart() { return new Coordinates(startX, startY); }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public VirtualPiece getPiece() {
        return piece;
    }

    public VirtualPiece getTakePiece() {
        return takePiece;
    }

    public void undo(VirtualBoard board) {
        undo(board, true);
    }
    public void undo(VirtualBoard board, boolean trueUndo) {
        abstractUndo(board);
        if (trueUndo) {
            Coordinates enPassant = board.getGame().getEnPassant();
            if ((enPassant == null && previousEnPassant != null) || (enPassant != null && !enPassant.equals(previousEnPassant))) {
                board.getGame().setEnPassant(previousEnPassant);
            }
            piece.unMoved();
        }
    }
    protected abstract void abstractUndo(VirtualBoard board);



    @Override
    public String toString() {
        return "Move{" +
                "piece=" + piece +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean isConnectedToOnePiece() {
        return connectedToOnePiece;
    }

    public boolean takesPiece() {
        return takePiece != null;
    }

    public boolean isLegal(VirtualBoard board) {
        doMove(board, false);
        boolean isLegal = !board.isTeamInCheck(piece.getTeam());
        undo(board);
        return isLegal;
    }
}
