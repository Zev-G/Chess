package tmw.me.chess.ui.program;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DropMenu<T> extends ChoiceBox<T> {

    private final ObservableList<T> items = FXCollections.observableArrayList();

    @SafeVarargs
    public DropMenu(T... items) {
        this(BigButton.DEFAULT_COLOR, items);
    }
    @SafeVarargs
    public DropMenu(Color mainColor, T... items) {
        this(BigButton.DEFAULT_WIDTH, BigButton.DEFAULT_HEIGHT, mainColor, BigButton.HoverEffect.GLOW, items);
    }
    @SafeVarargs
    public DropMenu(double minWidth, double minHeight, Color mainBgColor, BigButton.HoverEffect hoverEffect, T... items) {
        getStyleClass().add("drop-menu");
        setMinWidth(minWidth);
        setMinHeight(minHeight);
        setMaxHeight(minHeight);
        setMaxWidth(minWidth);

        setItems(this.items);
        this.items.addAll(items);

        Background bg = new Background(new BackgroundFill(mainBgColor, new CornerRadii(BigButton.CORNER_RADII), Insets.EMPTY));
        DropShadow dropShadow = new DropShadow(BlurType.THREE_PASS_BOX, mainBgColor.darker(), minHeight / 10, 1, 0, minHeight / 15);
        dropShadow.setWidth(0);

        setBackground(bg);
        setEffect(dropShadow);
        setCursor(BigButton.DEFAULT_CURSOR);

        if (hoverEffect == BigButton.HoverEffect.GLOW) {
            Glow glowEffect = new Glow(BigButton.HoverEffect.GLOW_LEVEL);
            glowEffect.setInput(dropShadow);
            setOnMouseEntered(mouseEvent -> setEffect(glowEffect));
            setOnMouseExited(mouseEvent -> setEffect(dropShadow));
        }
    }

}
