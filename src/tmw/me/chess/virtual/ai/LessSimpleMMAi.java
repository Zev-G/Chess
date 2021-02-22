package tmw.me.chess.virtual.ai;

import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;

import java.util.ArrayList;

public class LessSimpleMMAi extends MiniMaxAi {

    private static final double VALUE_OF_A_MOVE = 0.025;
    private static final double VALUE_OF_A_CHECK = 0.2;
    private static final double VALUE_OF_A_THREAT = 0.1;


    public LessSimpleMMAi(int depth, boolean useQuiescentSearch) {
        super(depth, useQuiescentSearch);
    }

    @Override
    public int situationValue(VirtualBoard board, String pos, Team turnOf) {
        ArrayList<Move> allLegalOpponentMoves = board.genMovesForTeam(turnOf.opposite());
        ArrayList<Move> allLegalMoves = board.genMovesForTeam(turnOf);

        VirtualPiece ourKing = turnOf == Team.WHITE ? board.getWhiteKing() : board.getBlackKing();
        VirtualPiece theirKing = turnOf == Team.WHITE ? board.getBlackKing() : board.getWhiteKing();
        if (allLegalMoves.isEmpty()) {
            Coordinates ourKingLoc = ourKing.getLocation();
            for (Move move : allLegalOpponentMoves) {
                if (move.getLoc().equals(ourKingLoc)) {
                    return -999999;
                }
            }
        }
        if (allLegalOpponentMoves.isEmpty()) {
            Coordinates theirKingLoc = theirKing.getLocation();
            for (Move move : allLegalMoves) {
                if (move.getLoc().equals(theirKingLoc)) {
                    return 999999;
                }
            }
            return 0;
        }
        if (board.getGame().isRepetition(pos)) {
            System.out.println("Returned 0 from repetition");
            return 0;
        }
        double val = 0;
        for (VirtualPiece piece : board.getWhitePieces()) {
            if (turnOf == Team.WHITE) {
                val += piece.getPieceType().getVal();
            } else {
                val -= piece.getPieceType().getVal();
            }
        }
        for (VirtualPiece piece : board.getBlackPieces()) {
            if (turnOf == Team.WHITE) {
                val -= piece.getPieceType().getVal();
            } else {
                val += piece.getPieceType().getVal();
            }
        }
        double moveDifference = allLegalMoves.size() - allLegalOpponentMoves.size();
        val += moveDifference * VALUE_OF_A_MOVE;
        return (int) (val * 100);
    }

    //For testing
//    @Override
//    public double situationValue(VirtualBoard board, Team turnOf) {
//        return 0;
//    }

}
