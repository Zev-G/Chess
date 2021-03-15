package tmw.me.chess.virtual;

import javafx.scene.image.Image;
import tmw.me.chess.ui.images.Images;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;
import tmw.me.chess.virtual.moves.MoveGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class VirtualPiece {

    private final PieceType pieceType;
    private final Team team;
    private final VirtualBoard board;

    private final MoveGenerator[] moveGenerators;

    private int x;
    private int y;

    private int movedTimes = 0;

    public VirtualPiece(PieceType pieceType, Team team, VirtualBoard board, int x, int y) {
        this.pieceType = pieceType;
        this.team = team;
        this.board = board;
        this.x = x;
        this.y = y;

        moveGenerators = PieceType.moveGeneratorsForPiece(this, board);
    }

    public static VirtualPiece fromChar(char c, VirtualBoard board, int x, int y) {
        Team team = Character.isUpperCase(c) ? Team.WHITE : Team.BLACK;
        PieceType type = PieceType.fromChar(Character.toLowerCase(c));
        return new VirtualPiece(type, team, board, x, y);
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Team getTeam() {
        return team;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "pieceType=" + pieceType +
                ", team=" + team +
                ", loc=(" + x + ", " + y + ")" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualPiece piece = (VirtualPiece) o;
        return x == piece.x &&
                y == piece.y &&
                movedTimes == piece.movedTimes &&
                pieceType == piece.pieceType &&
                team == piece.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, team, x, y, movedTimes);
    }

    public Image genImage() {
        return Images.get(team.toString() + "/" + pieceType.toString().toLowerCase() + ".png");
    }

    public boolean hasMoved() {
        return movedTimes != 0;
    }

    public void moved() {
        movedTimes++;
    }

    public void unMoved() {
        if (movedTimes > 0)
            movedTimes--;
    }

    public VirtualBoard getBoard() {
        return board;
    }

    public MoveGenerator[] getMoveGenerators() {
        return moveGenerators;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (MoveGenerator generator : moveGenerators) {
            Collections.addAll(moves, generator.genMoves());
        }
        return moves;
    }

    public ArrayList<Move> genBiLegalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (MoveGenerator generator : moveGenerators) {
            Collections.addAll(moves, generator.genMovesWithoutCheckingIfLegal());
        }
        return moves;
    }

    public Coordinates getLocation() {
        return new Coordinates(x, y);
    }

    public VirtualPiece copy(VirtualBoard board) {
        VirtualPiece copy = new VirtualPiece(pieceType, team, board, x, y);
        copy.movedTimes = movedTimes;
        return copy;
    }

    public int getMovedTimes() {
        return movedTimes;
    }

    public char toCompactString() {
        return team == Team.WHITE ? Character.toUpperCase(pieceType.c) : pieceType.c;
    }
}
