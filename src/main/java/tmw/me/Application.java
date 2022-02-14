package tmw.me;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tmw.me.chess.game.Game;
import tmw.me.chess.ui.board.BoardSpot;
import tmw.me.chess.ui.Styles;
import tmw.me.chess.ui.program.Program;
import tmw.me.chess.virtual.VirtualBoard;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new Program()));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    boolean showing = true;
    private void openBlindMode(Stage primaryStage) {
        Game game = new Game(true);
        game.setBoard(VirtualBoard.defaultBoard(game));
        VBox box = new VBox(game.getVisualBoard());
        Scene scene = new Scene(box);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SHIFT) {
                showing = !showing;
                for (BoardSpot[] spots : game.getVisualBoard().getBoardSpots()) {
                    for (BoardSpot spot : spots) {
                        if (spot.getPiece() != null) {
                            spot.getPiece().setVisible(showing);
                        }
                    }
                }
            }
        });
        scene.getStylesheets().add(Styles.get("board"));
        game.applyWithScene(scene);
        primaryStage.setScene(scene);

        primaryStage.setWidth(700);
        primaryStage.setHeight(700);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
