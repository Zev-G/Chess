package sample.chess.virtual.moves;

import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.VirtualPiece;
import sample.chess.virtual.ai.MiniMaxAi;
import sample.chess.virtual.extra.Coordinates;

import java.util.ArrayList;

public class LessSimpleMMAi extends MiniMaxAi {

    private static final double VALUE_OF_A_MOVE = 0.025;
    private static final double VALUE_OF_A_CHECK = 0.2;
    private static final double VALUE_OF_A_THREAT = 0.1;

    public LessSimpleMMAi(int depth) {
        super(depth);
    }

    @Override
    public int situationValue(VirtualBoard board, Team turnOf) {
        ArrayList<Move> allLegalOpponentMoves = board.genMovesForTeam(turnOf.opposite());
        ArrayList<Move> allLegalMoves = board.genMovesForTeam(turnOf);

        VirtualPiece ourKing = turnOf == Team.WHITE ? board.getWhiteKing() : board.getBlackKing();
        VirtualPiece theirKing = turnOf == Team.WHITE ? board.getBlackKing() : board.getWhiteKing();
        if (allLegalMoves.isEmpty()) {
            Coordinates ourKingLoc = ourKing.getLocation();
            for (Move move : allLegalOpponentMoves) {
                if (move.getLoc().equals(ourKingLoc)) {
                    return turnOf == Team.WHITE ? -9999 : 9999;
                }
            }
        }
        if (allLegalOpponentMoves.isEmpty()) {
            Coordinates theirKingLoc = theirKing.getLocation();
            for (Move move : allLegalMoves) {
                if (move.getLoc().equals(theirKingLoc)) {
                    return turnOf == Team.WHITE ? 9999 : -9999;
                }
            }
            return 0;
        }
        double val = board.boardValue();
        double moveDifference = allLegalMoves.size() - allLegalOpponentMoves.size();
        val += (moveDifference * VALUE_OF_A_MOVE) * turnOf.getAiNum();
        return (int) (val * 100);
    }

    //For testing
//    @Override
//    public double situationValue(VirtualBoard board, Team turnOf) {
//        return 0;
//    }

}
