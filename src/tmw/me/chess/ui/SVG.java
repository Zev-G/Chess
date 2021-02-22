package tmw.me.chess.ui;

public final class SVG {

    public static final String COPY_ICON = "M 8 0.5 h -6 c -0.5525 0 -1 0.4475 -1 1 v 7 h 1 v -7 h 6 v -1 z m 1.5 2 h -5.5 c -0.5525 0 -1 0.4475 -1 1 v 7 c 0 0.5525 0.4475 1 1 1 h 5.5 c 0.5525 0 1 -0.4475 1 -1 v -7 c 0 -0.5525 -0.4475 -1 -1 -1 z m 0 8 h -5.5 v -7 h 5.5 v 7 z";
    public static final String LEFT_ARROW = "M 8.122 24 l -4.122 -4 l 8 -8 l -8 -8 l 4.122 -4 l 11.878 12 z";
    public static final String RIGHT_ARROW = "M 11.878 0 l 4.122 4 l -8 8 l 8 8 l -4.122 4 l -11.878 -12 z";
    public static final String HOME_ICON = "M 15.45 7 L 14 5.551 V 2 c 0 -0.55 -0.45 -1 -1 -1 h -1 c -0.55 0 -1 0.45 -1 1 v 0.553 L 9 0.555 C 8.727 0.297 8.477 0 8 0 S 7.273 0.297 7 0.555 L 0.55 7 C 0.238 7.325 0 7.562 0 8 c 0 0.563 0.432 1 1 1 h 1 v 6 c 0 0.55 0.45 1 1 1 h 3 v -5 c 0 -0.55 0.45 -1 1 -1 h 2 c 0.55 0 1 0.45 1 1 v 5 h 3 c 0.55 0 1 -0.45 1 -1 V 9 h 1 c 0.568 0 1 -0.437 1 -1 C 16 7.562 15.762 7.325 15.45 7 z";

    public static String resizePath(String path, double multiply) {
        StringBuilder builder = new StringBuilder();
        for (String spaceSplit : path.split(" ")) {
            builder.append(" ");
            try {
                double num = Double.parseDouble(spaceSplit);
                num *= multiply;
                builder.append(num);
            } catch (NumberFormatException exception) {
                builder.append(spaceSplit);
            }
        }
        return builder.substring(1);
    }

}
