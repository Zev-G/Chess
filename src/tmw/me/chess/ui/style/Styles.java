package tmw.me.chess.ui.style;

import javafx.scene.paint.Color;

public class Styles {

    public static String get(String name) {
        return Styles.class.getResource(name + (name.endsWith(".css") ? "" : ".css")).toExternalForm();
    }

    public static Color mixColorsWithAlpha(Color color1, Color color2, double opacity) {
        double red = (opacity * color2.getRed() + (1 - opacity) * color1.getRed());
        double green = (opacity * color2.getGreen() + (1 - opacity) * color1.getGreen());
        double blue = (opacity * color2.getBlue() + (1 - opacity) * color1.getBlue());
        return Color.rgb((int) (red * 255), (int) (green * 255), (int) (blue * 255));
    }

}
