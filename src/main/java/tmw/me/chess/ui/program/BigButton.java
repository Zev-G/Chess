package tmw.me.chess.ui.program;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BigButton extends Button {

    public static final double CORNER_RADII = 10;
    public static final double DEFAULT_HEIGHT = 45;
    public static final double DEFAULT_WIDTH = 270;

    public static final Cursor DEFAULT_CURSOR = Cursor.HAND;
    public static final Color DEFAULT_COLOR = Color.web("#312e2b").darker();
    public static final Font DEFAULT_FONT = Font.font("Montserrat", FontWeight.BLACK, 20);
    public static final Font DEFAULT_SMALL_FONT = Font.font("Montserrat", FontWeight.BLACK, 18);
    public static final Font DEFAULT_SMALLER_FONT = Font.font("Montserrat", FontWeight.BLACK, 14);

    public BigButton(String text) {
        this(text, DEFAULT_COLOR);
    }

    public BigButton(String text, Color mainColor) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, text, null, mainColor, HoverEffect.GLOW);
    }

    public BigButton(double minWidth, double minHeight, String text, Node graphic, Color mainBgColor, HoverEffect hoverEffect) {
        super(text, graphic);
        setMinWidth(minWidth);
        setMinHeight(minHeight);
        setMaxHeight(minHeight);
        setMaxWidth(minWidth);
        getStyleClass().add("big-button");

        Background bg = new Background(new BackgroundFill(mainBgColor, new CornerRadii(CORNER_RADII), Insets.EMPTY));
        DropShadow dropShadow = new DropShadow(BlurType.THREE_PASS_BOX, mainBgColor.darker(), minHeight / 10, 1, 0, minHeight / 15);
        dropShadow.setWidth(0);

        setTextFill(Color.WHITE);
        setBackground(bg);
        setEffect(dropShadow);
        setFont(DEFAULT_FONT);
        setCursor(DEFAULT_CURSOR);

        if (hoverEffect == HoverEffect.GLOW) {
            Glow glowEffect = new Glow(HoverEffect.GLOW_LEVEL);
            glowEffect.setInput(dropShadow);
            setOnMouseEntered(mouseEvent -> setEffect(glowEffect));
            setOnMouseExited(mouseEvent -> setEffect(dropShadow));
        }

    }


    public enum HoverEffect {
        NONE,
        GLOW;

        public static final double GLOW_LEVEL = 0.3;
    }

}
