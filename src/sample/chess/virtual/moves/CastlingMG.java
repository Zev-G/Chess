package sample.chess.virtual.moves;

import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.VirtualPiece;
import sample.chess.virtual.extra.Coordinates;

import java.util.ArrayList;

public class CastlingMG extends MoveGenerator {

    public CastlingMG(VirtualPiece piece, VirtualBoard board) {
        super(piece, board);
    }

    @Override
    public Move[] abstractGenMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Coordinates> enemyMoves = board.getSpotsThreatenedByTeam(piece.getTeam().opposite());
        VirtualPiece king = piece.getTeam() == Team.WHITE ? board.getWhiteKing() : board.getBlackKing();
        if (!board.isTeamInCheck(piece.getTeam()) && !king.hasMoved()) {
            if (piece.getTeam() == Team.BLACK) {
                VirtualPiece queenRook = board.getPieceAtLocation(0, 0);
                VirtualPiece kingRook = board.getPieceAtLocation(7, 0);
                // Castling King-Side
                if (kingRook != null && !kingRook.hasMoved() && !enemyMoves.contains(new Coordinates(5, 0)) && !enemyMoves.contains(new Coordinates(6, 0))
                    && !board.isPieceAtLocation(5, 0) && !board.isPieceAtLocation(6, 0)) {
                    moves.add(new CastleMove(piece, 6, 0, kingRook, 5, 0));
                }
                // Castling Queen-Side
                if (queenRook != null && !queenRook.hasMoved() && !enemyMoves.contains(new Coordinates(3, 0)) && !enemyMoves.contains(new Coordinates(2, 0))
                        && !board.isPieceAtLocation(2, 0) && !board.isPieceAtLocation(3, 0)) {
                    moves.add(new CastleMove(piece, 2, 0, queenRook, 3, 0));
                }
            } else {
                VirtualPiece queenRook = board.getPieceAtLocation(0, 7);
                VirtualPiece kingRook = board.getPieceAtLocation(7, 7);
                // Castling King-Side
                if (kingRook != null && !kingRook.hasMoved() && !enemyMoves.contains(new Coordinates(5, 7)) && !enemyMoves.contains(new Coordinates(6, 7))
                        && !board.isPieceAtLocation(5, 7) && !board.isPieceAtLocation(6, 7)) {
                    moves.add(new CastleMove(piece, 6, 7, kingRook, 5, 7));
                }
                // Castling Queen-Side
                if (queenRook != null && !queenRook.hasMoved() && !enemyMoves.contains(new Coordinates(3, 7)) && !enemyMoves.contains(new Coordinates(2, 7))
                        && !board.isPieceAtLocation(2, 7) && !board.isPieceAtLocation(3, 7)) {
                    moves.add(new CastleMove(piece, 2, 7, queenRook, 3, 7));
                }
            }
        }
        return moves.toArray(new Move[0]);
    }

    @Override
    public Move[] genThreateningMoves() {
        return new Move[0];
    }

    private static class CastleMove extends SimpleMove {

        private final VirtualPiece rook;
        private final int rookX;
        private final int rookY;
        private final int rookStartX;
        private final int rookStartY;

        public CastleMove(VirtualPiece piece, int x, int y, VirtualPiece rook, int rookX, int rooKY) {
            super(piece, x, y);
            this.rook = rook;
            this.rookX = rookX;
            this.rookY = rooKY;
            this.rookStartX = rook.getX();
            this.rookStartY = rook.getY();
        }

        @Override
        protected void abstractDoMove(VirtualBoard board) {
            super.abstractDoMove(board);
            board.movePiece(rook, rookX, rookY);
        }

        @Override
        public void abstractUndo(VirtualBoard board) {
            super.abstractUndo(board);
            board.movePiece(rook, rookStartX, rookStartY);
        }
    }

}
