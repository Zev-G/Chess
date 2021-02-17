package sample.chess.ui.game;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import sample.chess.game.Game;
import sample.chess.ui.style.Styles;

public class GameContainer extends AnchorPane {

    private static final double padding = 25;

    private final Game game;


    private final VBox boardPieces = new VBox();
    private final BorderPane boardTop = new BorderPane();

    public GameContainer(Game game) {
        this.game = game;

        getStylesheets().add(Styles.get("board"));
        getStyleClass().add("game-container");
        setPadding(new Insets(padding));

        getChildren().add(boardTop);
        AnchorPane.setTopAnchor(boardTop, 0D); AnchorPane.setBottomAnchor(boardTop, 0D);
        AnchorPane.setRightAnchor(boardTop, 0D); AnchorPane.setLeftAnchor(boardTop, 0D);

        initBoardTop();
    }

    private void initBoardTop() {
//        boardPieces.getChildren().addAll(game.getVisualBoard());
        boardTop.setCenter(game.getVisualBoard());
    }

}
