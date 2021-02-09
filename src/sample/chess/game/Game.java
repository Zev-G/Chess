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

import java.util.ArrayList;

public class Game {

    private Team currentTurn = Team.WHITE;
    private final VirtualBoard board = VirtualBoard.defaultBoard(this);
    private Board visualBoard;

    private AiBase blackController = new LessSimpleMMAi(4);
    private AiBase whiteController = null;

    private final ArrayList<String> positions = new ArrayList<>();

    private boolean gameOver = false;

    public Game(boolean createVisualBoard) {
        if (createVisualBoard) {
            getVisualBoard();
        }
        turnPulse();
    }

    public void show(Stage stage) {
        stage.setScene(new Scene(getVisualBoard()));
        stage.show();
    }

    public Board getVisualBoard() {
        if (visualBoard == null)
            visualBoard = new Board(board);
        return visualBoard;
    }

    public void setVisualBoard(Board visualBoard) {
        this.visualBoard = visualBoard;
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
        String compactString = board.toCompactString(currentTurn);
        positions.add(compactString);

        turnPulse();
        if (visualBoard != null) {
            for (BoardSpot[] spots : getVisualBoard().getBoardSpots()) {
                for (BoardSpot spot : spots) {
                    spot.getStyleClass().removeAll("from-spot", "to-spot");
                }
            }
            getVisualBoard().getBoardSpotAtSpot(move.getLoc()).getStyleClass().add("to-spot");
            getVisualBoard().getBoardSpotAtSpot(move.getStart()).getStyleClass().add("from-spot");
        }
    }

    public boolean isRepetition(String compactBoard) {
        int instances = 0;
        for (String pos : positions) {
            if (pos.equals(compactBoard)) {
                instances++;
                if (instances == 3)
                    return true;
            }
        }
        return false;
    }

    private void turnPulse() {
        if (board.genMovesForTeam(currentTurn).isEmpty()) {
            gameOver = true;
            return;
        }
        if (currentTurn == Team.WHITE && whiteController != null) {
            whiteController.move(board, Team.WHITE, visualBoard);
        } else if (currentTurn == Team.BLACK && blackController != null) {
            blackController.move(board, Team.BLACK, visualBoard);
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void createVisualBoard() {
        getVisualBoard();
    }
}
