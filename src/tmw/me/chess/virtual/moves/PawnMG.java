package tmw.me.chess.virtual.moves;


import tmw.me.chess.virtual.PieceType;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;

import java.util.ArrayList;

// TODO fix promotion undo issues and implement correct en passant stuff
public class PawnMG extends MoveGenerator {

    public PawnMG(VirtualPiece piece, VirtualBoard board) {
        super(piece, board);
    }

    @Override
    public Move[] abstractGenMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        // En Passant
        if (board.getGame().getEnPassant() != null) {
            VirtualPiece enPassantPiece = board.getPieceAtLocation(board.getGame().getEnPassant());
            if (enPassantPiece != null && enPassantPiece.getTeam() != piece.getTeam() && enPassantPiece.getY() == piece.getY() && Math.abs(piece.getX() - enPassantPiece.getX()) == 1) {
                moves.add(new EnPassant(piece, enPassantPiece, enPassantPiece.getX(), piece.getY() + piece.getTeam().getNum()));
            }
        }
//        if (piece.getX() > 0) {
//            VirtualPiece pieceToLeft = board.getPieceAtLocation(board.getGame().getEnPassant());
//            if (pieceToLeft != null && pieceToLeft.canBeTakenByEnPassant() && pieceToLeft.getTeam() != piece.getTeam()
//                && !board.isPieceAtLocation(piece.getX() - 1, piece.getY() + piece.getTeam().getNum())) {
//                moves.add(new EnPassant(piece, pieceToLeft, piece.getX() - 1, piece.getY() + piece.getTeam().getNum()));
//            }
//        }
//        if (piece.getX() < 7) {
//            VirtualPiece pieceToLeft = board.getPieceAtLocation(piece.getX() + 1, piece.getY());
//            if (pieceToLeft != null && pieceToLeft.canBeTakenByEnPassant() && pieceToLeft.getTeam() != piece.getTeam()
//                && !board.isPieceAtLocation(piece.getX() + 1, piece.getY() + piece.getTeam().getNum())) {
//                moves.add(new EnPassant(piece, pieceToLeft, piece.getX() + 1, piece.getY() + piece.getTeam().getNum()));
//            }
//        }
        // Move One/Two Forward
        if (piece.getY() < 7 && piece.getY() > 0 && !board.isPieceAtLocation(piece.getX(), piece.getY() + piece.getTeam().getNum())) {
            if (!piece.hasMoved() && !board.isPieceAtLocation(piece.getX(), piece.getY() + piece.getTeam().getNum() * 2)) {
                moves.add(new TwoForwardMove(piece, piece.getX(), piece.getY() + piece.getTeam().getNum() * 2));
            }
            moves.add(new PossiblePromotion(piece, PieceType.QUEEN, piece.getX(), piece.getY() + piece.getTeam().getNum()));
        }
        if (piece.getX() > 0 && piece.getY() < 7 && piece.getY() > 0 && board.isPieceAtLocationForTeam(piece.getTeam().opposite(),piece.getX() - 1, piece.getY() + piece.getTeam().getNum())) {
            moves.add(new PossiblePromotion(piece, PieceType.QUEEN, piece.getX() - 1, piece.getY() + piece.getTeam().getNum()));
        }
        if (piece.getX() < 7 && piece.getY() < 7 && piece.getY() > 0 && board.isPieceAtLocationForTeam(piece.getTeam().opposite(), piece.getX() + 1, piece.getY() + piece.getTeam().getNum())) {
            moves.add(new PossiblePromotion(piece, PieceType.QUEEN, piece.getX() + 1, piece.getY() + piece.getTeam().getNum()));
        }
        return moves.toArray(new Move[0]);
    }

    @Override
    public Move[] genThreateningMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        if (piece.getY() + piece.getTeam().getNum() >= 0 && piece.getY() + piece.getTeam().getNum() < 8) {
            if (piece.getX() > 0) {
                moves.add(new SimpleMove(piece, piece.getX() - 1, piece.getY() + piece.getTeam().getNum()));
            }
            if (piece.getX() < 7) {
                moves.add(new SimpleMove(piece, piece.getX() + 1, piece.getY() + piece.getTeam().getNum()));
            }
        }
        return moves.toArray(new Move[0]);
    }

    private static class EnPassant extends Move {

        private EnPassant(VirtualPiece piece, VirtualPiece takePiece, int x, int y) {
            super(piece, x, y);
            this.takePiece = takePiece;
        }

        @Override
        protected void abstractDoMove(VirtualBoard board) {
            board.movePiece(piece, x, y);
            board.forceKillPiece(takePiece);
        }

        @Override
        public void abstractUndo(VirtualBoard board) {
            board.movePiece(piece, startX, startY);
            board.addPiece(takePiece);
        }

    }

    private static class TwoForwardMove extends SimpleMove {

        private TwoForwardMove(VirtualPiece piece, int x, int y) {
            super(piece, x, y);
        }

        @Override
        protected void abstractDoMove(VirtualBoard board) {
            super.abstractDoMove(board);
            piece.getBoard().getGame().setEnPassant(getLoc());
        }

        @Override
        public void abstractUndo(VirtualBoard board) {
            super.abstractUndo(board);
            piece.getBoard().getGame().setEnPassant(previousEnPassant);
        }
    }

    private static class PossiblePromotion extends SimpleMove {

        private final PieceType type;
        private VirtualPiece newPiece;

        public PossiblePromotion(VirtualPiece piece, PieceType type, int x, int y) {
            super(piece, x, y);
            this.type = type;
        }

        @Override
        protected void abstractDoMove(VirtualBoard board) {
            if (y == 0 || y == 7) {
                if (takePiece != null) {
                    board.forceKillPiece(takePiece);
                }
                board.forceKillPiece(piece);
                newPiece = new VirtualPiece(type, piece.getTeam(), board, x, y);
                board.addPiece(newPiece);
            } else {
                super.abstractDoMove(board);
            }
        }

        @Override
        public void abstractUndo(VirtualBoard board) {
            if (y == 0 || y == 7) {
                board.forceKillPiece(newPiece);
                board.addPiece(piece, startX, startY);
                if (takePiece != null) {
                    board.addPiece(takePiece, x, y);
                }
            } else {
                super.abstractUndo(board);
            }
        }

    }

}
