package tmw.me.chess.ui.style;

public class Styles {

    public static String get(String name) {
        return Styles.class.getResource(name + (name.endsWith(".css") ? "" : ".css")).toExternalForm();
    }

}
