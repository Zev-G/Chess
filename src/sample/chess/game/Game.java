package sample.chess.game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.chess.ui.Board;
import sample.chess.ui.BoardSpot;
import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.ai.AiBase;
import sample.chess.virtual.moves.LessSimpleMMAi;
import sample.chess.virtual.moves.Move;

public class Game {

    private Team currentTurn = Team.WHITE;
    private final Board board = new Board(VirtualBoard.defaultBoard(), this);

    private AiBase blackController = new LessSimpleMMAi(4);
    private AiBase whiteController = null;

    private boolean gameOver = false;

    public Game() {
        turnPulse();
    }

    public void show(Stage stage) {
        stage.setScene(new Scene(board));
        stage.show();
    }

    public AiBase getWhiteController() {
        return whiteController;
    }
    public AiBase getBlackController() {
        return blackController;
    }

    public void setWhiteController(AiBase whiteController) {
        this.whiteController = whiteController;
    }
    public void setBlackController(AiBase blackController) {
        this.blackController = blackController;
    }

    public Team getCurrentTurn() {
        return currentTurn;
    }

    public AiBase teamsTeamController(Team team) {
        return team == Team.WHITE ? whiteController : blackController;
    }

    public void nextTurn(Move move) {
        currentTurn = currentTurn.opposite();
        turnPulse();
        for (BoardSpot[] spots : board.getBoardSpots()) {
            for (BoardSpot spot : spots) {
                spot.getStyleClass().removeAll("from-spot", "to-spot");
            }
        }
        board.getBoardSpotAtSpot(move.getLoc()).getStyleClass().add("to-spot");
        board.getBoardSpotAtSpot(move.getStart()).getStyleClass().add("from-spot");
    }

    private void turnPulse() {
        if (board.getVirtualBoard().genMovesForTeam(currentTurn).isEmpty()) {
            gameOver = true;
            return;
        }
        if (currentTurn == Team.WHITE && whiteController != null) {
            whiteController.move(board, Team.WHITE);
        } else if (currentTurn == Team.BLACK && blackController != null) {
            blackController.move(board, Team.BLACK);
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
