package sample.chess.ui.board;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import sample.chess.virtual.VirtualPiece;
import sample.chess.virtual.extra.Coordinates;
import sample.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Piece extends ImageView {

    private static final double SIZE_RATIO = 0.9;

    private final VirtualPiece vPiece;

    // Drag variables
    private double startX;
    private double startY;
    private BoardSpot hoverSpot;
    private boolean makeVisible = false;

    private final ImageView dragImage = new ImageView();
    private final BorderPane spotSimulator = new BorderPane();

    public Piece(VirtualPiece vPiece) {
        this.vPiece = vPiece;

        setImage(vPiece.genImage());
        getStyleClass().add("piece");

        setFitHeight(BoardSpot.DEFAULT_SIZE * SIZE_RATIO);
        setFitWidth((BoardSpot.DEFAULT_SIZE * SIZE_RATIO / getImage().getHeight()) * getImage().getWidth());

        dragImage.getStyleClass().add("piece");
        dragImage.setImage(getImage());
        dragImage.setFitHeight(getFitHeight());
        dragImage.setFitWidth(getFitWidth());

        spotSimulator.setMouseTransparent(true);
        spotSimulator.setCenter(dragImage);
        spotSimulator.setMinWidth(BoardSpot.DEFAULT_SIZE);
        spotSimulator.setMinHeight(BoardSpot.DEFAULT_SIZE);
    }

    public void clicked(MouseEvent event, Board board) {
        ArrayList<Move> moves = vPiece.genMoves();
        if (!moves.isEmpty()) {
            for (Move move : moves) {
                if (move.isConnectedToOnePiece()) {
                    Circle circle = new Circle();
                    if (move.takesPiece()) {
                        circle.setRadius(BoardSpot.DEFAULT_SIZE / 2.6);
                        circle.getStyleClass().add("take-circle");
                    } else {
                        circle.setRadius(BoardSpot.DEFAULT_SIZE / 6.5);
                        circle.getStyleClass().add("move-circle");
                    }
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
        spotSimulator.setLayoutX(parentBounds.getMinX());
        spotSimulator.setLayoutY(parentBounds.getMinY());
    }

    public void dragged(MouseEvent event, Board board, BoardSpot atSpot) {
        Bounds parentBounds = atSpot.localToScene(atSpot.getBoundsInLocal());
        spotSimulator.setLayoutX(parentBounds.getMinX() + (event.getSceneX() - startX));
        spotSimulator.setLayoutY(parentBounds.getMinY() + (event.getSceneY() - startY));

        BoardSpot previousHoverSpot = hoverSpot;
        if (event.getPickResult().getIntersectedNode() != null) {
            hoverSpot = BoardSpot.getBoardSpotParentRecursively(event.getPickResult().getIntersectedNode());
        }
        if (hoverSpot != null && previousHoverSpot != hoverSpot) {
            if (!hoverSpot.getStyleClass().contains("hover-spot")) {
                hoverSpot.getStyleClass().add("hover-spot");
            }
            if (previousHoverSpot != null) {
                previousHoverSpot.getStyleClass().remove("hover-spot");
            }
        }
    }

    public void dragReleased(MouseEvent event, Board board) {
        if (makeVisible) {
            setVisible(true);
            makeVisible = false;
        }
        board.getChildren().remove(spotSimulator);
        if (hoverSpot != null) {
            Coordinates hoverSpotLocation = board.locationOfBoardSpot(hoverSpot);
            if (board.getVirtualBoard().getGame().getCurrentTurn() == vPiece.getTeam() && board.getVirtualBoard().getGame().teamsTeamController(vPiece.getTeam()) == null) {
                for (Move move : vPiece.genMoves()) {
                    if (hoverSpotLocation.equals(move.getLoc())) {
                        move.doMove(board, true);
                        board.clearShapes();
                        break;
                    }
                }
            }
            hoverSpot.getStyleClass().remove("hover-spot");
            hoverSpot = null;
        }
    }

    public VirtualPiece getVirtualPiece() {
        return vPiece;
    }
}
