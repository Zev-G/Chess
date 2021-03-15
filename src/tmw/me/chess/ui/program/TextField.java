package tmw.me.chess.ui.program;

import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicReference;

public class TextField extends javafx.scene.control.TextField {

    private final AtomicReference<Effect> defaultEffect = new AtomicReference<>();

    public TextField(String text) {
        this(text, BigButton.DEFAULT_COLOR);
    }

    public TextField(String text, Color mainColor) {
        this(BigButton.DEFAULT_WIDTH * 1.8, BigButton.DEFAULT_HEIGHT, text, mainColor, BigButton.HoverEffect.GLOW);
    }

    public TextField(double minWidth, double minHeight, String text, Color mainBgColor, BigButton.HoverEffect hoverEffect) {
        super(text);
        setMinWidth(minWidth);
        setMinHeight(minHeight);
        setMaxHeight(minHeight);
        setMaxWidth(minWidth);

        Background bg = new Background(new BackgroundFill(mainBgColor, new CornerRadii(BigButton.CORNER_RADII), Insets.EMPTY));
        DropShadow dropShadow = new DropShadow(BlurType.THREE_PASS_BOX, mainBgColor.darker(), minHeight / 10, 1, 0, minHeight / 15);
        dropShadow.setWidth(0);

        setStyle("-fx-text-fill: white;");
        setBackground(bg);
        setEffect(dropShadow);
        setFont(BigButton.DEFAULT_SMALLER_FONT);

        if (hoverEffect == BigButton.HoverEffect.GLOW) {
            defaultEffect.set(dropShadow);
            Glow glowEffect = new Glow(BigButton.HoverEffect.GLOW_LEVEL);
            setOnMouseEntered(mouseEvent -> {
                glowEffect.setInput(getEffect());
                defaultEffect.set(getEffect());
                setEffect(glowEffect);
            });
            setOnMouseExited(mouseEvent -> setEffect(defaultEffect.get()));
        }
    }

    public AtomicReference<Effect> getDefaultEffect() {
        return defaultEffect;
    }
}
