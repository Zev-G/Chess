package sample.chess.virtual;

import sample.chess.virtual.moves.*;

public enum PieceType {
    PAWN(1, 'p'),
    KNIGHT(3, 'n'),
    BISHOP(3, 'b'),
    ROOK(5, 'r'),
    QUEEN(9, 'q'),
    KING(0, 'k');

    public static MoveGenerator[] moveGeneratorsForPiece(VirtualPiece piece, VirtualBoard board) {
        if (piece.getPieceType() == PAWN) {
            return new MoveGenerator[]{ new PawnMG(piece, board) };
        } else if (piece.getPieceType() == KNIGHT) {
            return new MoveGenerator[]{ new SimpleMG(piece, board, 1, 2), new SimpleMG(piece, board, 2, 1)
                                          , new SimpleMG(piece, board, -1, 2), new SimpleMG(piece, board, -2, 1)
                                          , new SimpleMG(piece, board, 1, -2), new SimpleMG(piece, board, 2, -1)
                                          , new SimpleMG(piece, board, -1, -2), new SimpleMG(piece, board, -2, -1)};
        } else if (piece.getPieceType() == BISHOP) {
            return new MoveGenerator[]{ new RepeatingMG(piece, board, 1, 1), new RepeatingMG(piece, board, -1, -1)
                                          , new RepeatingMG(piece, board, -1, 1), new RepeatingMG(piece, board, 1, -1) };
        } else if (piece.getPieceType() == ROOK) {
            return new MoveGenerator[]{ new RepeatingMG(piece, board, 1, 0), new RepeatingMG(piece, board, -1, 0)
                                          , new RepeatingMG(piece, board, 0, 1), new RepeatingMG(piece, board, 0, -1) };
        } else if (piece.getPieceType() == QUEEN) {
            return new MoveGenerator[]{
                                          // Rook
                                            new RepeatingMG(piece, board, 1, 0), new RepeatingMG(piece, board, -1, 0)
                                          , new RepeatingMG(piece, board, 0, 1), new RepeatingMG(piece, board, 0, -1)
                                          // Bishop
                                          , new RepeatingMG(piece, board, 1, 1), new RepeatingMG(piece, board, -1, -1)
                                          , new RepeatingMG(piece, board, -1, 1), new RepeatingMG(piece, board, 1, -1) };
        } else {
            // Must be king
            return new MoveGenerator[]{ new CastlingMG(piece, board),
                new SimpleMG(piece, board, -1, 1), new SimpleMG(piece, board, 0, 1), new SimpleMG(piece, board, 1, 1),
                new SimpleMG(piece, board, -1, 0), new SimpleMG(piece, board, 1, 0),
                new SimpleMG(piece, board, -1, -1), new SimpleMG(piece, board, 0, -1), new SimpleMG(piece, board, 1, -1)
            };
        }
    }

    final int val;
    final char c;

    PieceType(int val, char c) {
        this.val = val;
        this.c = c;
    }

    public int getVal() {
        return val;
    }

    public char getChar() {
        return c;
    }
}
