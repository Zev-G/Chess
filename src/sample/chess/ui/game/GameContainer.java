package sample.chess.ui.game;

import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;

public class GameContainer extends AnchorPane {

    private static final double padding = 25;

    public GameContainer() {
        setPadding(new Insets(padding));
    }

}
