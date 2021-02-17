package sample.chess.game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.chess.ui.board.Board;
import sample.chess.ui.board.BoardSpot;
import sample.chess.ui.game.GameContainer;
import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.ai.AiBase;
import sample.chess.virtual.extra.Coordinates;
import sample.chess.virtual.moves.LessSimpleMMAi;
import sample.chess.virtual.moves.Move;

import java.util.ArrayList;

public class Game {

    private Team currentTurn = Team.WHITE;
    private final VirtualBoard board = VirtualBoard.defaultBoard(this);
    private Board visualBoard;

    private AiBase blackController = new LessSimpleMMAi(2, false);
    private AiBase whiteController = null;

    private Coordinates enPassant;

    private int halfMoves = 0;
    private int moves = 1;

    private final ArrayList<String> positions = new ArrayList<>();

    private boolean gameOver = false;

    public Game(boolean createVisualBoard) {
        if (createVisualBoard) {
            getVisualBoard();
        }
        turnPulse();
    }

    public void show(Stage stage) {
        stage.setScene(new Scene(new GameContainer(this)));
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
        if (currentTurn == Team.WHITE)
            moves++;

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

    public Coordinates getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(Coordinates enPassant) {
        this.enPassant = enPassant;
    }

    public int getMoves() {
        return moves;
    }

    public int getHalfMoves() {
        return halfMoves;
    }
}
