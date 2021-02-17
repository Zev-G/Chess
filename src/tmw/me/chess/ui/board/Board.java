package tmw.me.chess.ui.board;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import tmw.me.chess.ui.extra.Arrow;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;

public class Board extends AnchorPane {

    private static final double MIN_SIZE = 100;

    // Technical
    private final VirtualBoard vBoard;
    private final BoardSpot[][] boardSpots = new BoardSpot[8][8];

    // Visual
    private final GridPane pieceHolder = new GridPane();
    private final Pane arrowPane = new Pane();

    private final DoubleProperty size = new SimpleDoubleProperty();

    public Board(VirtualBoard board) {
        this.vBoard = board;
        arrowPane.setMouseTransparent(true);
        getChildren().addAll(pieceHolder, arrowPane);

        size.addListener((observableValue, number, t1) -> {
            for (BoardSpot[] spots : boardSpots) {
                for (BoardSpot spot : spots) {
                    double newSize = t1.doubleValue() / 8;
                    spot.setMinWidth(newSize);
                    spot.setMinHeight(newSize);
                    spot.setMaxHeight(newSize);
                    spot.setMaxWidth(newSize);
                    Piece currentPiece = spot.getPiece();
                    if (currentPiece != null) {
                        currentPiece.setFitHeight(newSize * Piece.SIZE_RATIO);
                        currentPiece.setFitWidth((newSize * Piece.SIZE_RATIO / currentPiece.getImage().getHeight()) * currentPiece.getImage().getWidth());
                        currentPiece.getSpotSimulator().setMinWidth(spot.getWidth());
                        currentPiece.getSpotSimulator().setMinHeight(spot.getHeight());
                    }
                }
            }
            Platform.runLater(() -> {
                pieceHolder.setLayoutX(getWidth() - pieceHolder.getWidth() - ((getWidth() - pieceHolder.getWidth()) / 2));
                pieceHolder.setLayoutY(getHeight() - pieceHolder.getHeight() - ((getHeight() - pieceHolder.getHeight()) / 2));
            });
        });

        updateItemsToSize();
        widthProperty().addListener((observableValue, number, t1) -> updateItemsToSize());
        heightProperty().addListener((observableValue, number, t1) -> updateItemsToSize());
        Platform.runLater(this::updateItemsToSize);

        AnchorPane.setLeftAnchor(arrowPane, 0D); AnchorPane.setRightAnchor(arrowPane, 0D);
        AnchorPane.setBottomAnchor(arrowPane, 0D); AnchorPane.setTopAnchor(arrowPane, 0D);

        setMinWidth(0);
        minWidthProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() != 0) {
                setMinWidth(0);
            }
        });
        setMinHeight(0);
        minHeightProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() != 0) {
                setMinHeight(0);
            }
        });

        initBoardSpots();
        populateGridPane();
    }

    private void updateItemsToSize() {
        double width = getWidth();
        double height = getHeight();
        if (Math.min(width, height) >= MIN_SIZE) {
            size.set(Math.min(width, height));
            pieceHolder.setLayoutX(width - pieceHolder.getWidth() - ((width - pieceHolder.getWidth()) / 2));
            pieceHolder.setLayoutY(height - pieceHolder.getHeight() - ((height - pieceHolder.getHeight()) / 2));
        }
    }

    public void bindToSize(DoubleProperty other) {
        other.bind(size);
    }

    private void initBoardSpots() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BoardSpot spot = new BoardSpot(SpotColor.fromBoardSpot(i, j), this);
                if (i == 0) {
                    spot.setNum(8 - j);
                }
                if (j == 7) {
                    spot.setLetter(Coordinates.letterFromInt(i));
                }
                boardSpots[i][j] = spot;
                pieceHolder.add(spot, i, j);
            }
        }
    }

    public Bounds getBoundsInScene() {
        return localToScene(getBoundsInLocal());
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
                VirtualPiece piece = vBoard.getPieceAtLocation(i, j);
                if (piece != null) {
                    if (spot.getPiece() == null || spot.getPiece().getVirtualPiece() != piece) {
                        spot.setCurrentPiece(new Piece(piece));
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

    public void clearShapes() {
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
        double minX1 = spot1.getBoundsInScene().getMinX() - getBoundsInScene().getMinX();
        double minY1 = spot1.getBoundsInScene().getMinY() - getBoundsInScene().getMinY();
        double minX2 = spot2.getBoundsInScene().getMinX() - getBoundsInScene().getMinX();
        double minY2 = spot2.getBoundsInScene().getMinY() - getBoundsInScene().getMinY();
        Arrow arrow = new Arrow(minX1 + spot1.getMinWidth() / 2,
                minY1 + spot1.getMinWidth() / 2,
                minX2 + spot2.getMinWidth() / 2,
                minY2 + spot2.getMinHeight() / 2, 0.04375 * size.get(), 0.03125 * size.get());
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
        pane.setMinHeight(spot.getHeight());
        pane.setMinWidth(spot.getWidth());
        Bounds bounds = spot.getBoundsInScene();
        double minX = bounds.getMinX() - getBoundsInScene().getMinX();
        double minY = bounds.getMinY() - getBoundsInScene().getMinY();
        getChildren().add(pane);
        pane.setLayoutX(minX);
        pane.setLayoutY(minY);
        move.doMove(this, true);
        Piece pieceAtSpot = getBoardSpotAtSpot(move.getX(), move.getY()).getPiece();
        pieceAtSpot.setVisible(false);
        Platform.runLater(() -> {
            Piece a = getBoardSpotAtSpot(move.getX(), move.getY()).getPiece();
            a.setVisible(false);
        });
        Platform.runLater(this::clearShapes);
        Bounds newPieceBounds = pieceAtSpot.localToScene(pieceAtSpot.getBoundsInLocal());
        double newMinX = newPieceBounds.getMinX() - getBoundsInScene().getMinX();
        double newMinY = newPieceBounds.getMinY() - getBoundsInScene().getMinY();
        double transitionLength = 250;
        Timeline timeline = new Timeline(new KeyFrame(new Duration(transitionLength), actionEvent -> {
            getChildren().remove(pane);
            pieceAtSpot.setVisible(true);
            piece.setVisible(true);
            this.setMouseTransparent(false);
        }, new KeyValue(pane.layoutXProperty(), newMinX), new KeyValue(pane.layoutYProperty(), newMinY)));
        timeline.play();
    }
}
