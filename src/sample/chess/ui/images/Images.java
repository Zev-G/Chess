package sample.chess.ui.images;

import javafx.scene.image.Image;

public class Images {

    public static Image get(String name) {
        return new Image(Images.class.getResource(name).toExternalForm());
    }

}
