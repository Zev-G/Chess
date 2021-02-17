package tmw.me.chess.ui.board;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Shape;

public class BoardSpot extends AnchorPane {

    private Piece currentPiece;
    private Shape currentShape;

    private final BorderPane piecePane = new BorderPane();
    private final BorderPane shapePane = new BorderPane();
    private final AnchorPane textPane = new AnchorPane();

    private final SpotColor color;
    private final Board board;


    public BoardSpot(SpotColor color, Board board) {
        this.color = color;
        this.board = board;

        getStyleClass().addAll("board-spot", "board-spot-" + color.toString().toLowerCase());
        textPane.getStyleClass().add("board-spot-text-pane");
//        getChildren().addAll(shapePane, textPane);
        getChildren().addAll(piecePane, shapePane, textPane);

        textPane.setPadding(new Insets(7.5));
        textPane.setMouseTransparent(true);

//        setMinHeight(DEFAULT_SIZE);
//        setMinWidth(DEFAULT_SIZE);
//        setMaxHeight(DEFAULT_SIZE);
//        setMaxWidth(DEFAULT_SIZE);

        AnchorPane.setLeftAnchor(piecePane, 0D); AnchorPane.setRightAnchor(piecePane, 0D);
        AnchorPane.setTopAnchor(piecePane, 0D); AnchorPane.setBottomAnchor(piecePane, 0D);
        AnchorPane.setLeftAnchor(shapePane, 0D); AnchorPane.setRightAnchor(shapePane, 0D);
        AnchorPane.setTopAnchor(shapePane, 0D); AnchorPane.setBottomAnchor(shapePane, 0D);
        AnchorPane.setLeftAnchor(textPane, 0D); AnchorPane.setRightAnchor(textPane, 0D);
        AnchorPane.setTopAnchor(textPane, 0D); AnchorPane.setBottomAnchor(textPane, 0D);

        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                board.clearArrows();
                if (currentShape != null) {
                    currentShape.getOnMousePressed().handle(mouseEvent);
                } else if (currentPiece != null && board.getVirtualBoard().getGame().getCurrentTurn() == currentPiece.getVirtualPiece().getTeam() && board.getVirtualBoard().getGame().teamsTeamController(currentPiece.getVirtualPiece().getTeam()) == null && !board.getVirtualBoard().getGame().isGameOver()) {
                    board.clearShapes();
                    currentPiece.clicked(mouseEvent, board, this);
                } else {
                    board.clearShapes();
                }
            }
        });

        setOnDragDetected(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (currentPiece != null) {
                    currentPiece.dragDetected(mouseEvent, board, this);
                }
            }
        });

        setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (currentPiece != null) {
                    currentPiece.dragged(mouseEvent, board, this);
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

    public void setNum(int num) {
        Label numLabel = sizeConnectedLabel(0.22);
        numLabel.setText(Integer.toString(num));
        numLabel.getStyleClass().add(color == SpotColor.LIGHT ? "dark-spot-label" : "light-spot-label");
        textPane.getChildren().add(numLabel);
        AnchorPane.setTopAnchor(numLabel, 0D); AnchorPane.setLeftAnchor(numLabel, 0D);
    }
    public void setLetter(char c) {
        Label numLabel = sizeConnectedLabel(0.22);
        numLabel.setText(Character.toString(c));
        numLabel.getStyleClass().add(color == SpotColor.LIGHT ? "dark-spot-label" : "light-spot-label");
        textPane.getChildren().add(numLabel);
        AnchorPane.setBottomAnchor(numLabel, 0D); AnchorPane.setRightAnchor(numLabel, 0D);
    }
    public Label sizeConnectedLabel(double ratio) {
        Label label = new Label();
        heightProperty().addListener((observableValue, number, t1) -> label.setStyle("-fx-font-size: " + ((int) (t1.doubleValue() * ratio)) + ";"));
        return label;
    }

    public void setCurrentPiece(Piece piece) {
        currentPiece = piece;
        piecePane.setCenter(piece);
        if (piece != null) {
            currentPiece.setFitHeight(getHeight() * Piece.SIZE_RATIO);
            currentPiece.setFitWidth((getWidth() * Piece.SIZE_RATIO / currentPiece.getImage().getHeight()) * currentPiece.getImage().getWidth());
            piece.getSpotSimulator().setMinWidth(getWidth());
            piece.getSpotSimulator().setMinHeight(getHeight());
        }
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
