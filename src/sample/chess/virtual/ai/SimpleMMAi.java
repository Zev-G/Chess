package sample.chess.virtual.ai;

import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;

public class SimpleMMAi extends MiniMaxAi {

    private static final double VALUE_OF_A_MOVE = 0.025;
    private static final double VALUE_OF_A_CHECK = 0.2;
    private static final double VALUE_OF_A_THREAT = 0.1;
    private static final double VALUE_OF_A_UNPROTECTED_THREAT = 0.25;

    public SimpleMMAi(int depth) {
        super(depth, false);
    }

    @Override
    public int situationValue(VirtualBoard board, String pos, Team turnOf) {
//        double val = board.boardValue();
//        if (board.isTeamInCheckmate(turnOf.opposite())) {
//            return turnOf == Team.WHITE ? 9999 : -9999;
//        } else if (board.isTeamInCheckmate(turnOf)) {
//            return turnOf == Team.WHITE ? -9999 : 9999;
//        }
//        ArrayList<Move> moves = board.genMovesForTeam(turnOf);
//        ArrayList<Move> enemyProtectingMoves = board.genMovesForTeam(turnOf.opposite());
//        for (int i = enemyProtectingMoves.size() - 1; i >= 0; i--) {
//            if (!board.isPieceAtLocationForTeam(turnOf.opposite(), enemyProtectingMoves.get(i).getLoc())) {
//                enemyProtectingMoves.remove(enemyProtectingMoves.get(i));
//            }
//        }
//        if (board.isTeamInStaleMate(turnOf))
//            return 0;
//        double val = board.boardValue();
//        double moveDifference = board.getSpotsTeamCanMoveTo(turnOf).size() - board.getSpotsTeamCanMoveTo(turnOf.opposite()).size();
//        val += (moveDifference * VALUE_OF_A_MOVE) * turnOf.getAiNum();
//        if (board.isTeamInCheck(turnOf.opposite())) {
//            val += VALUE_OF_A_CHECK;
//        }
//        main: for (Move move : moves) {
//            if (board.isPieceAtLocationForTeam(turnOf.opposite(), move.getLoc())) {
//                val += VALUE_OF_A_THREAT * turnOf.getAiNum();
//                for (Move protectingMove : enemyProtectingMoves) {
//                    if (protectingMove.getLoc().equals(move.getLoc())) {
//                        continue main;
//                    }
//                }
//                val += VALUE_OF_A_UNPROTECTED_THREAT * turnOf.getAiNum();
//            }
//        }
        return board.boardValue();
//        return val;
    }
}
