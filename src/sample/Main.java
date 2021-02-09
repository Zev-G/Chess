package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.chess.game.Game;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Game game = new Game(true);
        game.show(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
