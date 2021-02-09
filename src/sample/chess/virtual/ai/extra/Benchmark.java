package sample.chess.virtual.ai.extra;

import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.VirtualPiece;
import sample.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.Calendar;

public class Benchmark {

    public static long millisToSimulateXMoves(int moves, VirtualBoard board) {
        ArrayList<Move> movesList = board.genMovesForTeam(Team.WHITE);
        Move move = movesList.get(0);
        long startTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < moves; i++) {
            move.doMove(board, false);
            move.undo(board);
        }
        return Calendar.getInstance().getTimeInMillis() - startTime;
    }

    public static long millisToCreateMoves(int moves, VirtualBoard board, VirtualPiece test) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < moves; i++) {
            test.genMoves();
        }
        return Calendar.getInstance().getTimeInMillis() - startTime;
    }

    public static long millisToCreateBiLegalMoves(int moves, VirtualBoard board, VirtualPiece test) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < moves; i++) {
            test.genBiLegalMoves();
        }
        return Calendar.getInstance().getTimeInMillis() - startTime;
    }

}
