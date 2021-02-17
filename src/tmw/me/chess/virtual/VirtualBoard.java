package tmw.me.chess.virtual;

import tmw.me.chess.game.Game;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;
import tmw.me.chess.virtual.moves.MoveGenerator;

import java.util.ArrayList;
import java.util.Arrays;

public class VirtualBoard {


    private VirtualPiece[][] board;

    private final Game game;
    private final ArrayList<VirtualPiece> blackPieces = new ArrayList<>();
    private final ArrayList<VirtualPiece> whitePieces = new ArrayList<>();

    private VirtualPiece whiteKing;
    private VirtualPiece blackKing;

    private VirtualPiece qswRook;
    private VirtualPiece kswRook;
    private VirtualPiece qsbRook;
    private VirtualPiece ksbRook;

    public VirtualBoard(Game game) {
        this.game = game;
    }
    public VirtualBoard(VirtualPiece[][] board, Game game) {
        this.game = game;
        this.board = board;
        init();
    }

    public Game getGame() {
        return game;
    }

    private void init() {
        // Init. variables: whiteKing, blackKing, whitePieces, blackPieces
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                VirtualPiece piece = getPieceAtLocation(i, j);
                if (piece != null) {
                    if (piece.getTeam() == Team.WHITE) {
                        whitePieces.add(piece);
                        if (piece.getPieceType() == PieceType.ROOK) {
                            if (piece.getX() == 0) {
                                qswRook = piece;
                            } else if (piece.getX() == 7) {
                                kswRook = piece;
                            }
                        }
                    } else {
                        blackPieces.add(piece);
                        if (piece.getPieceType() == PieceType.ROOK) {
                            if (piece.getX() == 0) {
                                qsbRook = piece;
                            } else if (piece.getX() == 7) {
                                ksbRook = piece;
                            }
                        }
                    }
                    if (piece.getPieceType() == PieceType.KING) {
                        if (piece.getTeam() == Team.WHITE) {
                            whiteKing = piece;
                        } else {
                            blackKing = piece;
                        }
                    }

                }
            }
        }
    }

    public void initBoard(VirtualPiece[][] board) {
        if (this.board != null) {
            System.err.println("Can't set value of VirtualBoard's board when VirtualBoard's board already has a value. Only use initBoard() to do step-by-step initialization of a VirtualBoard.");
            return;
        }
        this.board = board;
        init();
    }

    public boolean isPieceAtLocation(Coordinates coords) {
        return isPieceAtLocation(coords.getX(), coords.getY());
    }
    public boolean isPieceAtLocation(int x, int y) {
        return getPieceAtLocation(x, y) != null;
    }
    public boolean isPieceAtLocationForTeam(Team team, Coordinates coords) {
        return isPieceAtLocationForTeam(team, coords.getX(), coords.getY());
    }
    public boolean isPieceAtLocationForTeam(Team team, int x, int y) {
        VirtualPiece piece = getPieceAtLocation(x, y);
        return piece != null && piece.getTeam() == team;
    }

    public VirtualPiece getPieceAtLocation(Coordinates coords) {
        return getPieceAtLocation(coords.getX(), coords.getY());
    }
    public VirtualPiece getPieceAtLocation(int x, int y) {
        if (board[x].length < y + 1) {
            return null;
        }
        return board[x][y];
    }

    public VirtualPiece[][] getBoardArray() {
        return board;
    }

    private static VirtualBoard duplicateBoard(VirtualPiece[][] board, Game game) {
        VirtualBoard newBoard = new VirtualBoard(game);
        VirtualPiece[][] newBoardArray = new VirtualPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoardArray[i][j] = board[i][j] != null ? board[i][j].copy(newBoard) : null;
            }
        }
        newBoard.initBoard(newBoardArray);
        return newBoard;
    }

    public static VirtualBoard defaultBoard(Game game) {
        VirtualBoard board = new VirtualBoard(game);
        VirtualPiece[][] boardValue = new VirtualPiece[][] {
                { new VirtualPiece(PieceType.ROOK, Team.BLACK, board, 0, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 0, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 0, 6), new VirtualPiece(PieceType.ROOK, Team.WHITE, board, 0, 7) },
                { new VirtualPiece(PieceType.KNIGHT, Team.BLACK, board, 1, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 1, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 1, 6), new VirtualPiece(PieceType.KNIGHT, Team.WHITE, board, 1, 7) },
                { new VirtualPiece(PieceType.BISHOP, Team.BLACK, board, 2, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 2, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 2, 6), new VirtualPiece(PieceType.BISHOP, Team.WHITE, board, 2, 7) },
                { new VirtualPiece(PieceType.QUEEN, Team.BLACK, board, 3, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 3, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 3, 6), new VirtualPiece(PieceType.QUEEN, Team.WHITE, board, 3, 7) },
                { new VirtualPiece(PieceType.KING, Team.BLACK, board, 4, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 4, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 4, 6), new VirtualPiece(PieceType.KING, Team.WHITE, board, 4, 7) },
                { new VirtualPiece(PieceType.BISHOP, Team.BLACK, board, 5, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 5, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 5, 6), new VirtualPiece(PieceType.BISHOP, Team.WHITE, board, 5, 7) },
                { new VirtualPiece(PieceType.KNIGHT, Team.BLACK, board, 6, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 6, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 6, 6), new VirtualPiece(PieceType.KNIGHT, Team.WHITE, board, 6, 7) },
                { new VirtualPiece(PieceType.ROOK, Team.BLACK, board, 7, 0), new VirtualPiece(PieceType.PAWN, Team.BLACK, board, 7, 1), null, null, null, null, new VirtualPiece(PieceType.PAWN, Team.WHITE, board, 7, 6), new VirtualPiece(PieceType.ROOK, Team.WHITE, board, 7, 7) }
        };
        board.initBoard(boardValue);
        return board;
    }

    private static String printBoard(VirtualPiece[][] board) {
        StringBuilder text = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            text.append("\nY: ").append(y).append("{");
            for (int x = 0; x < 8; x++) {
                text.append("X: ").append(x).append("{").append(board[x][y]).append("}, ");
            }
            text.append("}");
        }
        return text.toString();
    }

    private static VirtualPiece[] eightNulls() {
        return new VirtualPiece[]{ null, null, null, null, null, null, null, null };
    }

    public void movePiece(VirtualPiece piece, int x, int y) {
        VirtualPiece pieceAtSpot = getPieceAtLocation(x, y);
        if (pieceAtSpot != null && pieceAtSpot != piece) {
            if (pieceAtSpot.getTeam() == Team.WHITE) {
                whitePieces.remove(pieceAtSpot);
            } else {
                blackPieces.remove(pieceAtSpot);
            }
        }
        board[piece.getX()][piece.getY()] = null;
        piece.setX(x);
        piece.setY(y);
        board[x][y] = piece;
    }



    public ArrayList<VirtualPiece> getBlackPieces() {
        return blackPieces;
    }

    public ArrayList<VirtualPiece> getWhitePieces() {
        return whitePieces;
    }

    public VirtualPiece getBlackKing() {
        return blackKing;
    }

    public VirtualPiece getWhiteKing() {
        return whiteKing;
    }

    public ArrayList<Coordinates> getSpotsThreatenedByTeam(Team team) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (team == Team.WHITE) {
            for (VirtualPiece piece : whitePieces) {
                for (MoveGenerator generator : piece.getMoveGenerators()) {
                    for (Move move : generator.genThreateningMoves()) {
                        coordinates.add(new Coordinates(move.getX(), move.getY()));
                    }
                }
            }
        } else {
            for (VirtualPiece piece : blackPieces) {
                for (MoveGenerator generator : piece.getMoveGenerators()) {
                    for (Move move : generator.genThreateningMoves()) {
                        coordinates.add(new Coordinates(move.getX(), move.getY()));
                    }
                }
            }
        }
        return coordinates;
    }

    public ArrayList<Coordinates> getSpotsTeamCanMoveTo(Team team) {
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        if (team == Team.WHITE) {
            for (VirtualPiece piece : (ArrayList<VirtualPiece>) whitePieces.clone()) {
                for (MoveGenerator generator : piece.getMoveGenerators()) {
                    for (Move move : generator.genMoves()) {
                        coordinates.add(new Coordinates(move.getX(), move.getY()));
                    }
                }
            }
        } else {
            for (VirtualPiece piece : (ArrayList<VirtualPiece>) blackPieces.clone()) {
                for (MoveGenerator generator : piece.getMoveGenerators()) {
                    for (Move move : generator.genMoves()) {
                        coordinates.add(new Coordinates(move.getX(), move.getY()));
                    }
                }
            }
        }
        return coordinates;
    }

    public boolean movePutsInCheck(Move move) {
        boolean putsInCheck;
        move.doMove(this, false);
        putsInCheck = isTeamInCheck(move.getPiece().getTeam());
        move.undo(this);
        return putsInCheck;
    }

    public boolean isTeamInCheck(Team team) {
        Coordinates kingCoords = team == Team.WHITE ? whiteKing.getLocation() : blackKing.getLocation();
        for (VirtualPiece piece : team == Team.WHITE ? blackPieces : whitePieces) {
            for (MoveGenerator gen : piece.getMoveGenerators()) {
                for (Move move : gen.genThreateningMoves()) {
                    if (move.getLoc().equals(kingCoords))
                        return true;
                }
            }
        }
        return false;
    }

    public void forceKillPiece(VirtualPiece piece) {
        if (piece.getTeam() == Team.WHITE) {
            whitePieces.remove(piece);
        } else {
            blackPieces.remove(piece);
        }
        board[piece.getX()][piece.getY()] = null;
    }

    public void addPiece(VirtualPiece piece) {
        addPiece(piece, piece.getX(), piece.getY());
    }
    public void addPiece(VirtualPiece piece, int x, int y) {
        if (piece.getTeam() == Team.WHITE) {
            whitePieces.add(piece);
        } else {
            blackPieces.add(piece);
        }
        board[x][y] = piece;
    }

    public VirtualBoard copy() {
        return duplicateBoard(board, game);
    }

    public ArrayList<Move> genMovesForTeam(Team team) {
        ArrayList<Move> moves = new ArrayList<>();
        for (VirtualPiece piece : team == Team.WHITE ? (ArrayList<VirtualPiece>) whitePieces.clone() : (ArrayList<VirtualPiece>) blackPieces.clone()) {
            moves.addAll(piece.genMoves());
        }
        return moves;
    }
    public ArrayList<Move> genBiLegalMovesForTeam(Team team) {
        ArrayList<Move> moves = new ArrayList<>();
        for (VirtualPiece piece : team == Team.WHITE ? (ArrayList<VirtualPiece>) whitePieces.clone() : (ArrayList<VirtualPiece>) blackPieces.clone()) {
            moves.addAll(piece.genBiLegalMoves());
        }
        return moves;
    }
    public ArrayList<Move> genTakeMovesForTeam(Team team) {
        ArrayList<Move> moves = new ArrayList<>();
        for (VirtualPiece piece : team == Team.WHITE ? (ArrayList<VirtualPiece>) whitePieces.clone() : (ArrayList<VirtualPiece>) blackPieces.clone()) {
            for (Move move : piece.genMoves()) {
                if (move.getTakePiece() != null)
                    moves.add(move);
            }
        }
        return moves;
    }

    public boolean isTeamInCheckmate(Team checkIsInCheckMate) {
        return genMovesForTeam(checkIsInCheckMate).isEmpty() && isTeamInCheck(checkIsInCheckMate);
    }

    public boolean isTeamInStaleMate(Team turn) {
        return genMovesForTeam(turn).isEmpty() && !isTeamInCheck(turn);
    }

    public int boardValue() {
        int val = 0;
        for (VirtualPiece piece : whitePieces) {
            val += piece.getPieceType().getVal();
        }
        for (VirtualPiece piece : blackPieces) {
            val -= piece.getPieceType().getVal();
        }
        return val;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualBoard that = (VirtualBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    public boolean boardEquals(VirtualPiece[][] otherBoard) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (otherBoard[x][y] == null && board[x][y] != null)
                    return false;
                if (otherBoard[x][y] != null && !otherBoard[x][y].equals(board[x][y]))
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "VirtualBoard{" + Arrays.deepToString(board) + '}';
    }

    public String toCompactString(Team turnOf) {
        StringBuilder builder = new StringBuilder();
        for (VirtualPiece[] pieces : board) {
            for (VirtualPiece piece : pieces) {
                builder.append(piece != null ? piece.toCompactString() : "e");
            }
        }
        builder.append("/").append(canCastleKingSide(Team.WHITE) ? "y" : "n");
        builder.append(canCastleKingSide(Team.BLACK) ? "y" : "n");
        builder.append(canCastleQueenSide(Team.WHITE) ? "y" : "n");
        builder.append(canCastleQueenSide(Team.BLACK) ? "y" : "n");
        builder.append(" ").append(turnOf.toString().charAt(0));
        return builder.toString();
    }

    public String toFen() {
        StringBuilder fen = new StringBuilder();
        for (int y = 0; y < 8; y++) {
            byte blank = 0;
            for (int x = 0; x < 8; x++) {
                VirtualPiece atLoc = getPieceAtLocation(x, y);
                if (atLoc == null)
                    blank++;
                else {
                    if (blank > 0) {
                        fen.append(blank);
                    }
                    fen.append(atLoc.toCompactString());
                    blank = 0;
                }
            }
            if (blank > 0)
                fen.append(blank);
            fen.append('/');
        }
        fen.append(" ").append(game.getCurrentTurn().toString().substring(0, 1).toLowerCase()).append(" ");
        boolean wKingSide = canCastleKingSide(Team.WHITE);
        boolean bKingSide = canCastleKingSide(Team.BLACK);
        boolean wQueenSide = canCastleQueenSide(Team.WHITE);
        boolean bQueenSide = canCastleQueenSide(Team.BLACK);
        if (!wKingSide && !bKingSide && !wQueenSide && !bQueenSide)
            fen.append("-");
        else {
            if (wKingSide)
                fen.append("K");
            if (wQueenSide)
                fen.append("Q");
            if (bKingSide)
                fen.append("k");
            if (bQueenSide)
                fen.append("q");
        }
        fen.append(" ");
        Coordinates enPassant = game.getEnPassant();
        if (enPassant != null) {
            fen.append(enPassant.toString());
        } else {
            fen.append("-");
        }
        fen.append(" ").append(game.getHalfMoves()).append(" ").append(game.getMoves());
        return fen.toString();
    }

    public void toFen(String fen) {

    }

    public static void fromFen(String fen, Game game) {

    }

    // These methods only make sure the king and rook haven't moved, which is why they are private.
    private boolean canCastleKingSide(Team team) {
        return team == Team.WHITE ? (!kswRook.hasMoved() && !whiteKing.hasMoved()) : (!ksbRook.hasMoved() && !blackKing.hasMoved());
    }
    private boolean canCastleQueenSide(Team team) {
        return team == Team.WHITE ? (!qswRook.hasMoved() && !whiteKing.hasMoved()) : (!qsbRook.hasMoved() && !blackKing.hasMoved());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
