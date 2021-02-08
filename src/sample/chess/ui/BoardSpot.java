package sample.chess.ui;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Shape;

public class BoardSpot extends AnchorPane {

    public static final double DEFAULT_SIZE = 100;

    private Piece currentPiece;
    private Shape currentShape;

    private final BorderPane piecePane = new BorderPane();
    private final BorderPane shapePane = new BorderPane();

    private final SpotColor color;
    private final Board board;

    public BoardSpot(SpotColor color, Board board) {
        this.color = color;
        this.board = board;

        getStyleClass().addAll("board-spot", "board-spot-" + color.toString().toLowerCase());
        getChildren().addAll(piecePane, shapePane);

        setMinHeight(DEFAULT_SIZE);
        setMinWidth(DEFAULT_SIZE);
        setMaxHeight(DEFAULT_SIZE);
        setMaxWidth(DEFAULT_SIZE);

        AnchorPane.setLeftAnchor(piecePane, 0D); AnchorPane.setRightAnchor(piecePane, 0D);
        AnchorPane.setTopAnchor(piecePane, 0D); AnchorPane.setBottomAnchor(piecePane, 0D);
        AnchorPane.setLeftAnchor(shapePane, 0D); AnchorPane.setRightAnchor(shapePane, 0D);
        AnchorPane.setTopAnchor(shapePane, 0D); AnchorPane.setBottomAnchor(shapePane, 0D);

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                board.clearArrows();
                if (currentShape != null) {
                    currentShape.getOnMousePressed().handle(mouseEvent);
                } else if (currentPiece != null && board.getGame().getCurrentTurn() == currentPiece.getVirtualPiece().getTeam() && board.getGame().teamsTeamController(currentPiece.getVirtualPiece().getTeam()) == null && !board.getGame().isGameOver()) {
                    board.clearShapes();
                    currentPiece.clicked(mouseEvent, board);
                } else {
                    board.clearShapes();
                }
            }
        });

        setOnDragDetected(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (currentPiece != null) {
                    currentPiece.dragDetected(mouseEvent, board);
                }
            }
        });

        setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (currentPiece != null) {
                    currentPiece.dragged(mouseEvent, board);
                    if (mouseEvent.getPickResult().getIntersectedNode() == null || mouseEvent.getPickResult().getIntersectedNode() instanceof Board) {
                        for (BoardSpot[] spots : board.getBoardSpots()) {
                            for (BoardSpot spot : spots) {
                                spot.getStyleClass().remove("hover-spot");
                            }
                        }
                    }
                }
            }
        });

        setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (currentPiece != null) {
                    currentPiece.dragReleased(mouseEvent, board);
                }
                for (BoardSpot[] spots : board.getBoardSpots()) {
                    for (BoardSpot spot : spots) {
                        spot.getStyleClass().remove("hover-spot");
                    }
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                BoardSpot endSpot = getBoardSpotParentRecursively(mouseEvent.getPickResult().getIntersectedNode());
                if (endSpot != null && endSpot != this) {
//                    Arrow arrow = new Arrow(getBoundsInScene().getMinX() + getMinWidth() / 2,
//                                            getBoundsInScene().getMinY() + getMinWidth() / 2,
//                                            endSpot.getBoundsInScene().getMinX() + endSpot.getMinWidth() / 2,
//                                        endSpot.getBoundsInScene().getMinY() + endSpot.getMinHeight() / 2, 35, 25);
//                    board.getArrowPane().getChildren().add(arrow);
                    board.drawArrow(board.locationOfBoardSpot(this), board.locationOfBoardSpot(endSpot));
                } else if (endSpot == this) {
                    if (this.getStyleClass().contains("highlighted-spot")) {
                        this.getStyleClass().remove("highlighted-spot");
                    } else {
                        this.getStyleClass().add("highlighted-spot");
                    }
                }
            }
        });
    }

    public void setCurrentPiece(Piece piece) {
        currentPiece = piece;
        piecePane.setCenter(piece);
    }

    public void setCurrentShape(Shape shape) {
        currentShape = shape;
        shapePane.setCenter(shape);
    }

    public SpotColor getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return currentPiece;
    }

    public Node getCurrentShape() {
        return currentShape;
    }

    public Bounds getBoundsInScene() {
        return localToScene(getBoundsInLocal());
    }

    public static BoardSpot getBoardSpotParentRecursively(Node start) {
        Node currentNode = start;
        if (currentNode == null)
            return null;
        while (currentNode.getParent() != null) {
            if (currentNode instanceof BoardSpot) {
                return (BoardSpot) currentNode;
            } else {
                currentNode = currentNode.getParent();
            }
        }
        return null;
    }

}
