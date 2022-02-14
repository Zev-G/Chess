package tmw.me;

public final class Resources {

    public static final String FONTS_PATH = "fonts";
    public static final String IMAGES_PATH = "images";
    public static final String STYLE_PATH = "style";

    public static String css(String fileName) {
        if (fileName == null) return null;
        if (!fileName.endsWith(".css")) fileName = fileName + ".css";
        if (!fileName.startsWith(STYLE_PATH + "/") || !fileName.startsWith(STYLE_PATH + "\\")) fileName = STYLE_PATH + "/" + fileName;
        return get(fileName);
    }

    public static String image(String fileName) {
        if (fileName == null) return null;
        if (!fileName.startsWith(IMAGES_PATH + "/") || !fileName.startsWith(IMAGES_PATH + "\\")) fileName = IMAGES_PATH + "/" + fileName;
        return get(fileName);
    }

    public static String font(String fileName) {
        if (fileName == null) return null;
        if (!fileName.startsWith(FONTS_PATH + "/") || !fileName.startsWith(FONTS_PATH + "\\")) fileName = FONTS_PATH + "/" + fileName;
        return get(fileName);
    }

    public static String get(String name) {
        return Resources.class.getClassLoader().getResource(name).toExternalForm();
    }

}
