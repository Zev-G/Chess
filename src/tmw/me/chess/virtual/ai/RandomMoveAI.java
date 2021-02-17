package tmw.me.chess.virtual.ai;

import javafx.application.Platform;
import tmw.me.chess.ui.board.Board;
import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RandomMoveAI extends AiBase {

    public RandomMoveAI() {
        super();
    }

    @Override
    public void move(VirtualBoard board, Team team, Board visualBoard) {
        ArrayList<Move> moves = board.genMovesForTeam(team);
        Move move = moves.get((int) (Math.random() * ((double) moves.size() - 1)));
        if (visualBoard != null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> visualBoard.animateMove(move, visualBoard.getBoardSpotAtSpot(move.getPiece().getLocation()).getPiece()));
                }
            }, 300);
        } else {
            move.doMove(board, true);
        }
    }

}
