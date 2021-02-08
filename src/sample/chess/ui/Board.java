package sample.chess.ui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.chess.game.Game;
import sample.chess.ui.extra.Arrow;
import sample.chess.ui.style.Styles;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.extra.Coordinates;
import sample.chess.virtual.moves.Move;

public class Board extends Pane {

    // Technical
    private VirtualBoard vBoard;
    private final BoardSpot[][] boardSpots = new BoardSpot[8][8];
    private final Game game;

    // Visual
    private final GridPane pieceHolder = new GridPane();
    private final Pane arrowPane = new Pane();

    public Board(VirtualBoard board, Game game) {
        this.game = game;
        this.vBoard = board;
        getStylesheets().add(Styles.get("board"));
        arrowPane.setMouseTransparent(true);
        getChildren().addAll(pieceHolder, arrowPane);
        AnchorPane.setTopAnchor(pieceHolder, 10D); AnchorPane.setBottomAnchor(pieceHolder, 10D);
        AnchorPane.setRightAnchor(pieceHolder, 10D); AnchorPane.setLeftAnchor(pieceHolder, 10D);

        initBoardSpots();
        populateGridPane();
    }

    private void initBoardSpots() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardSpot spot = new BoardSpot(SpotColor.fromBoardSpot(i, j), this);
                boardSpots[i][j] = spot;
                pieceHolder.add(spot, i, j);
            }
        }
    }

    public void populateGridPane() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (vBoard.isPieceAtLocation(i, j)) {
                    BoardSpot spot = boardSpots[i][j];
                    spot.setCurrentPiece(new Piece(vBoard.getPieceAtLocation(i, j)));
                }
            }
        }
    }

    public VirtualBoard getVirtualBoard() {
        return vBoard;
    }

    public void updateToBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardSpot spot = boardSpots[i][j];
                if (vBoard.isPieceAtLocation(i, j)) {
                    if (spot.getPiece() == null || spot.getPiece().getVirtualPiece() != vBoard.getPieceAtLocation(i, j)) {
                        spot.setCurrentPiece(new Piece(vBoard.getPieceAtLocation(i, j)));
                    }
                } else {
                    spot.setCurrentPiece(null);
                }
            }
        }
    }

    public BoardSpot[][] getBoardSpots() {
        return boardSpots;
    }
    public BoardSpot getBoardSpotAtSpot(int x, int y) {
        return boardSpots[x][y];
    }

    public BoardSpot getBoardSpotAtSpot(Coordinates loc) {
        return getBoardSpotAtSpot(loc.getX(), loc.getY());
    }

    public Game getGame() {
        return game;
    }

    public void clearShapes() {
        // TODO make this also clear all children other than the ones which are supposed to be in this node's children list. so all imageviews would be removed. aka fix the issue where if your dragging a piece and ai takes then the boardspot receives a mouse released but the piece there is no the piece which was dragging and so the piece stays.
        for (BoardSpot[] spots : boardSpots) {
            for (BoardSpot spot : spots) {
                spot.setCurrentShape(null);
            }
        }
    }

    public Coordinates locationOfBoardSpot(BoardSpot spot) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardSpots[x][y] == spot) {
                    return new Coordinates(x, y);
                }
            }
        }
        return null;
    }

    public void drawArrow(Coordinates coords1, Coordinates coords2) {
        drawArrow(coords1.getX(), coords1.getY(), coords2.getX(), coords2.getY());
    }
    public Arrow drawArrow(int x1, int y1, int x2, int y2) {
        BoardSpot spot1 = getBoardSpotAtSpot(x1, y1);
        BoardSpot spot2 = getBoardSpotAtSpot(x2, y2);
        Arrow arrow = new Arrow(spot1.getBoundsInScene().getMinX() + spot1.getMinWidth() / 2,
                spot1.getBoundsInScene().getMinY() + spot1.getMinWidth() / 2,
                spot2.getBoundsInScene().getMinX() + spot2.getMinWidth() / 2,
                spot2.getBoundsInScene().getMinY() + spot2.getMinHeight() / 2, 35, 25);
        getArrowPane().getChildren().add(arrow);
        return arrow;
    }
    public Pane getArrowPane() {
        return arrowPane;
    }
    public void removeArrow(Arrow arrow) {
        arrowPane.getChildren().remove(arrow);
    }
    public void clearArrows() {
        arrowPane.getChildren().clear();
        for (BoardSpot[] spots : boardSpots) {
            for (BoardSpot spot : spots) {
                spot.getStyleClass().remove("highlighted-spot");
            }
        }
    }

    // Very messy code (Beware)
    public void animateMove(Move move, Piece piece) {
        BoardSpot spot = getBoardSpotAtSpot(piece.getVirtualPiece().getLocation());
        this.setMouseTransparent(true);
        piece.setVisible(false);
        ImageView pieceImage = new ImageView(piece.getImage());
        pieceImage.setFitHeight(piece.getFitHeight());
        pieceImage.setFitWidth(piece.getFitWidth());
        BorderPane pane = new BorderPane();
        pane.setCenter(pieceImage);
        pane.setMinHeight(BoardSpot.DEFAULT_SIZE);
        pane.setMinWidth(BoardSpot.DEFAULT_SIZE);
        Bounds bounds = spot.localToScene(spot.getBoundsInLocal());
        getChildren().add(pane);
        pane.setLayoutX(bounds.getMinX());
        pane.setLayoutY(bounds.getMinY());
        move.doMove(this, true);
        Piece pieceAtSpot = getBoardSpotAtSpot(move.getX(), move.getY()).getPiece();
        pieceAtSpot.setVisible(false);
        Platform.runLater(() -> {
            Piece a = getBoardSpotAtSpot(move.getX(), move.getY()).getPiece();
            a.setVisible(false);
        });
        Platform.runLater(this::clearShapes);
        Bounds newPieceBounds = pieceAtSpot.localToScene(pieceAtSpot.getBoundsInLocal());
        double transitionLength = 250;
        Timeline timeline = new Timeline(new KeyFrame(new Duration(transitionLength), actionEvent -> {
            getChildren().remove(pane);
            pieceAtSpot.setVisible(true);
            piece.setVisible(true);
            this.setMouseTransparent(false);
        }, new KeyValue(pane.layoutXProperty(), newPieceBounds.getMinX()), new KeyValue(pane.layoutYProperty(), newPieceBounds.getMinY())));
        timeline.play();
    }
}
