package tmw.me.chess.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public final class Testing {

    public static void testPane(Pane node) {
        node.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        node.setBorder(new Border(new BorderStroke(Color.rgb(255, 0, 0, 0.2), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.EMPTY)));
    }

}
