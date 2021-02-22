package tmw.me.chess.ui.game;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tmw.me.chess.game.Game;
import tmw.me.chess.ui.style.PowerfulStyler;
import tmw.me.chess.ui.style.Styles;

public class GameContainer extends AnchorPane {

    private static final double padding = 25;

    private final Game game;

    private final VBox boardPieces = new VBox();
    private final BorderPane boardTop = new BorderPane();

    private final PowerfulStyler styler = new PowerfulStyler(this);

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

    public Bounds getBoundsInScene() {
        return localToScene(getBoundsInLocal());
    }

    public void setPrimaryColor(Color c) {
        styler.setStyleValue("-light-color", toRGBCode(c));
        styler.setStyleAndUpdate("-light-move-spot-color", toRGBCode(Styles.mixColorsWithAlpha(c, Color.rgb(255, 255, 0), 0.5)));
    }
    public void setSecondaryColor(Color c) {
        styler.setStyleValue("-dark-color", toRGBCode(c));
        styler.setStyleAndUpdate("-dark-move-spot-color", toRGBCode(Styles.mixColorsWithAlpha(c, Color.rgb(255, 255, 0), 0.5)));
    }
    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    private void initBoardTop() {
//        boardPieces.getChildren().addAll(game.getVisualBoard());
        boardTop.setCenter(game.getVisualBoard());
    }

    public Game getGame() {
        return game;
    }
}
