package sample.chess.virtual.ai;

import javafx.application.Platform;
import sample.chess.ui.Board;
import sample.chess.virtual.Team;
import sample.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RandomMoveAI extends AiBase {

    public RandomMoveAI() {
        super();
    }

    @Override
    public void move(Board board, Team team) {
        ArrayList<Move> moves = board.getVirtualBoard().genMovesForTeam(team);
        Move move = moves.get((int) (Math.random() * ((double) moves.size() - 1)));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> board.animateMove(move, board.getBoardSpotAtSpot(move.getPiece().getLocation()).getPiece()));
            }
        }, 300);
    }

}
