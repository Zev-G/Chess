package tmw.me.chess.game;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import tmw.me.chess.ui.board.Board;
import tmw.me.chess.ui.board.BoardSpot;
import tmw.me.chess.ui.game.GameContainer;
import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.ai.AiBase;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;

import java.util.ArrayList;

public class Game {

    public static final String DEFAULT_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private Team currentTurn = Team.WHITE;
    private VirtualBoard board;
    private Board visualBoard;

    private AiBase blackController = null;
    private AiBase whiteController = null;

    private final boolean createVisualBoard;

    private Coordinates enPassant;

    private int halfMoves = 0;
    private int moveCount = 1;

    private final ArrayList<String> positions = new ArrayList<>();
    private final ArrayList<Move> moves = new ArrayList<>();
    private int varianceLocation = 0;

    private boolean gameOver = false;

    public Game(boolean createVisualBoard) {
        this.createVisualBoard = createVisualBoard;
    }

    public void start() {
        if (board == null) {
            board = VirtualBoard.defaultBoard(this);
        }
        if (createVisualBoard) {
            getVisualBoard();
        }
        turnPulse();
    }

    public void setBoard(VirtualBoard board) {
        this.board = board;
    }

    public void show(Stage stage) {
        Scene scene = new Scene(new GameContainer(this));
        applyWithScene(scene);
        stage.setScene(scene);
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

    public boolean goBack(boolean animate) {
        if (animate && !visualBoard.isVisible())
            return false;
        // Insure we can go back any further
        if (moves.size() + varianceLocation >= 1) {
            varianceLocation--;
            Move move = moves.get(moves.size() + varianceLocation);
            if (!animate) {
                move.undo(board, false);
            }
            if (visualBoard != null) {
                if (moves.size() + varianceLocation >= 1) {
                    applyHighlightingForMove(moves.get(moves.size() + varianceLocation - 1));
                } else {
                    clearHighlighting();
                }
                if (animate) {
                    visualBoard.animateMoveUndo(move);
                } else {
                    visualBoard.updateToBoard();
                }
            }
            return true;
        }
        return false;
    }
    public boolean goForward(boolean animate) {
        if (animate && !visualBoard.isVisible())
            return false;
        if (varianceLocation < 0) {
            varianceLocation++;
            Move move = moves.get(moves.size() + varianceLocation - 1);
            if (!animate) {
                move.justMove(board);
            }
            if (visualBoard != null) {
                applyHighlightingForMove(moves.get(moves.size() + varianceLocation - 1));
                if (animate) {
                    visualBoard.animateMovement(move, visualBoard.getBoardSpotAtSpot(move.getPiece().getLocation()).getPiece(), move.getX(), move.getY());
                } else {
                    visualBoard.updateToBoard();
                }
            }
            return true;
        }
        return false;
    }
    public boolean isAtMostRecentMove() {
        return varianceLocation == 0;
    }
    public void goToMostRecentMove() {
        while (varianceLocation != 0) {
            goBack(false);
        }
    }

    public void applyHighlightingForMove(Move move) {
        clearHighlighting();
        getVisualBoard().getBoardSpotAtSpot(move.getLoc()).getStyleClass().add("move-spot");
        getVisualBoard().getBoardSpotAtSpot(move.getStart()).getStyleClass().add("move-spot");
    }
    public void clearHighlighting() {
        for (BoardSpot[] spots : getVisualBoard().getBoardSpots()) {
            for (BoardSpot spot : spots) {
                spot.getStyleClass().remove("move-spot");
            }
        }
    }

    public void nextTurn(Move move) {
        if (gameOver)
            return;
        currentTurn = currentTurn.opposite();
        String compactString = board.toCompactString(currentTurn);
        positions.add(compactString);
        moves.add(move);
        if (currentTurn == Team.WHITE)
            moveCount++;
        if (!isAtMostRecentMove()) {
            goToMostRecentMove();
        }
        turnPulse();
        if (visualBoard != null) {
            applyHighlightingForMove(move);
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
        if (gameOver)
            return;
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
        return moveCount;
    }

    public int getHalfMoves() {
        return halfMoves;
    }

    public void setCurrentTurn(Team currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setMoves(int moves) {
        this.moveCount = moves;
    }

    public void setHalfMoves(int halfMoves) {
        this.halfMoves = halfMoves;
    }

    public String toFen() {
        return board.toFen();
    }

    public static Game fromFen(String fen, boolean createVisualBoard) {
        Game game = new Game(createVisualBoard);
        VirtualBoard preInitBoard = new VirtualBoard(game);
        VirtualPiece[][] board = VirtualBoard.emptyBoard();
        fen = VirtualBoard.applyFenToArray(board, fen, preInitBoard);
        assert fen != null;
        preInitBoard.initBoard(board);
        game.setBoard(preInitBoard);
        char turnOf = fen.charAt(1);
        game.setCurrentTurn(turnOf == 'b' ? Team.BLACK : Team.WHITE); // Starting with 'b' so that the default (if it for some reason isn't b or w) is white.
        boolean castleRightsExist = fen.charAt(2) != '-';
        int endOfCastleRights = 0;
        if (castleRightsExist) {
            boolean ksw = false;
            boolean qsw = false;
            boolean ksb = false;
            boolean qsb = false;
            for (int i = 0; i < 4; i++) {
                char currentChar = fen.charAt(3 + i);
                endOfCastleRights = 3 + i;
                if (currentChar == ' ') {
                    endOfCastleRights = 2 + i;
                    break;
                }
                if (currentChar == 'K') {
                    ksw = true;
                } else if (currentChar == 'Q') {
                    qsw = true;
                } else if (currentChar == 'k') {
                    ksb = true;
                } else if (currentChar == 'q') {
                    qsb = true;
                }
            }
            if (!ksw && preInitBoard.getKswRook() != null)
                preInitBoard.getKswRook().moved();
            if (!qsw && preInitBoard.getQswRook() != null)
                preInitBoard.getQswRook().moved();
            if (!ksb && preInitBoard.getKsbRook() != null)
                preInitBoard.getKsbRook().moved();
            if (!qsb && preInitBoard.getQsbRook() != null)
                preInitBoard.getQsbRook().moved();
        } else {
            endOfCastleRights = 3;
            preInitBoard.getKsbRook().moved();
            preInitBoard.getKswRook().moved();
            preInitBoard.getQsbRook().moved();
            preInitBoard.getQswRook().moved();
        }
        fen = fen.substring(endOfCastleRights + 2);
        if (fen.charAt(0) != '-') {
            game.setEnPassant(Coordinates.fromString(fen.substring(0, 2)));
            fen = fen.substring(4);
        } else {
            fen = fen.substring(2);
        }
        int halfMoves = Integer.parseInt(Character.toString(fen.charAt(0)));
        int fullMoves = Integer.parseInt(Character.toString(fen.charAt(2)));
        game.setHalfMoves(halfMoves);
        game.setMoves(fullMoves);
        return game;
    }

    public void applyWithScene(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                goBack(true);
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                goForward(true);
            }
        });
    }

    public void stop() {
        gameOver = true;
    }
}
