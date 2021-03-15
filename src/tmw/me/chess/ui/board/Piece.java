package tmw.me.chess.ui.board;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.extra.Coordinates;
import tmw.me.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Piece extends ImageView {

    public static final double SIZE_RATIO = 0.9;

    private final VirtualPiece vPiece;

    // Drag variables
    private double startX;
    private double startY;
    private BoardSpot hoverSpot;
    private boolean makeVisible = false;
    private boolean wasVisible = true;

    private final ImageView dragImage = new ImageView();
    private final BorderPane spotSimulator = new BorderPane();

    public Piece(VirtualPiece vPiece) {
        this.vPiece = vPiece;

        setImage(vPiece.genImage());
        getStyleClass().add("piece");


        dragImage.getStyleClass().add("piece");

        dragImage.setImage(getImage());
        dragImage.fitHeightProperty().bind(fitHeightProperty());
        dragImage.fitWidthProperty().bind(fitWidthProperty());

        spotSimulator.setMouseTransparent(true);
        spotSimulator.setCenter(dragImage);
    }

    public BorderPane getSpotSimulator() {
        return spotSimulator;
    }

    public void clicked(MouseEvent event, Board board, BoardSpot spot) {
        ArrayList<Move> moves = vPiece.genMoves();
        wasVisible = isVisible();
        if (!moves.isEmpty()) {
            for (Move move : moves) {
                if (move.isConnectedToOnePiece()) {
                    Circle circle = new Circle();
                    if (move.takesPiece()) {
                        circle.setRadius(spot.getHeight() / 2.6);
                        circle.getStyleClass().add("take-circle");
                        circle.setStrokeWidth(spot.getHeight() / 10);
                    } else {
                        circle.setRadius(spot.getHeight() / 6.5);
                        circle.getStyleClass().add("move-circle");
                    }
                    ChangeListener<Number> listener = (observableValue, number, t1) -> {
                        if (circle.getParent() != null) {
                            if (move.takesPiece()) {
                                circle.setRadius(t1.doubleValue() / 2.6);
                                circle.setStrokeWidth(t1.doubleValue() / 10);
                            } else {
                                circle.setRadius(t1.doubleValue() / 6.5);
                            }
                        }
                    };
                    spot.heightProperty().addListener(listener);
                    spot.parentProperty().addListener((observableValue, parent, t1) -> {
                        if (t1 == null) {
                            spot.heightProperty().removeListener(listener);
                        }
                    });
                    AtomicBoolean pressed = new AtomicBoolean(false);
                    circle.setOnMousePressed(mouseEvent -> {
                        if (!pressed.get()) {
                            pressed.set(true);
                            board.animateMove(move, this);
                        }
                    });
                    board.getBoardSpotAtSpot(move.getLoc()).setCurrentShape(circle);
                }
            }
        }
    }

    public void dragDetected(MouseEvent event, Board board, BoardSpot spot) {
        setVisible(false);
        makeVisible = true;
        board.getChildren().add(spotSimulator);
        startX = event.getSceneX();
        startY = event.getSceneY();
        Bounds parentBounds = spot.localToScene(spot.getBoundsInLocal());
        Bounds boardBounds = board.getBoundsInScene();
        spotSimulator.setLayoutX(parentBounds.getMinX() - boardBounds.getMinX());
        spotSimulator.setLayoutY(parentBounds.getMinY() - boardBounds.getMinY());
    }

    public void dragged(MouseEvent event, Board board, BoardSpot atSpot) {
        Bounds parentBounds = atSpot.localToScene(atSpot.getBoundsInLocal());
        Bounds boardBounds = board.getBoundsInScene();
        spotSimulator.setLayoutX(parentBounds.getMinX() - boardBounds.getMinX() + (event.getSceneX() - startX));
        spotSimulator.setLayoutY(parentBounds.getMinY() - boardBounds.getMinY() + (event.getSceneY() - startY));

        BoardSpot previousHoverSpot = hoverSpot;
        if (event.getPickResult().getIntersectedNode() != null) {
            hoverSpot = BoardSpot.getBoardSpotParentRecursively(event.getPickResult().getIntersectedNode());
        }
        if (hoverSpot != null && previousHoverSpot != hoverSpot) {
            if (hoverSpot.getBorder() == null) {
                hoverSpot.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(hoverSpot.getHeight() * 0.05))));
            }
            if (previousHoverSpot != null) {
                previousHoverSpot.setBorder(null);
            }
        }
    }

    public void dragReleased(MouseEvent event, Board board) {
        if (makeVisible) {
            setVisible(wasVisible);
            makeVisible = false;
        }
        board.getChildren().remove(spotSimulator);
        if (hoverSpot != null) {
            Coordinates hoverSpotLocation = board.locationOfBoardSpot(hoverSpot);
            if (board.getVirtualBoard().getGame().getCurrentTurn() == vPiece.getTeam() && board.getVirtualBoard().getGame().teamsTeamController(vPiece.getTeam()) == null && board.getVirtualBoard().getGame().isAtMostRecentMove()) {
                for (Move move : vPiece.genMoves()) {
                    if (hoverSpotLocation.equals(move.getLoc())) {
                        move.doMove(board, true);
                        board.clearShapes();
                        break;
                    }
                }
            }
            hoverSpot.setBorder(null);
            hoverSpot = null;
        }
    }

    public VirtualPiece getVirtualPiece() {
        return vPiece;
    }

    public ImageView getDragImage() {
        return dragImage;
    }
}
