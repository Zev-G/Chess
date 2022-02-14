package tmw.me.chess.ui;

import javafx.scene.image.Image;
import tmw.me.Resources;

public class Images {

    public static Image get(String name) {
        return new Image(Resources.image(name));
    }

}
