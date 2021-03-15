package tmw.me;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tmw.me.chess.game.Game;
import tmw.me.chess.ui.board.BoardSpot;
import tmw.me.chess.ui.style.Styles;
import tmw.me.chess.virtual.VirtualBoard;

public class Main extends Application {

    boolean showing = true;

    @Override
    public void start(Stage primaryStage) {

//        Game game = new Game(true);
//        game.start();
//        game.show(primaryStage);

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

//        primaryStage.setScene(new Scene(new Program()));
        primaryStage.setWidth(700);
        primaryStage.setHeight(700);
        primaryStage.show();
//        System.out.println(game.getVisualBoard().getVirtualBoard().toFen());
//
//        Button combine = new Button("Combine");
//        ColorPicker pickerA = new ColorPicker();
//        ColorPicker pickerB = new ColorPicker();
//        Circle colorContainer = new Circle(50, Color.WHITE);
//        combine.setOnAction(actionEvent -> {
//
//            Color a = pickerA.getValue();
//            Color b = pickerB.getValue();
//            Color color = Color.rgb((int) ((a.getRed() + b.getRed()) / 2 * 255), (int) ((a.getGreen() + b.getGreen()) / 2 * 255),(int) ((a.getBlue() + b.getBlue()) / 2 * 255));
//            colorContainer.setFill(color);
//            System.out.println(a + " + " + b + " = " + color);
//        });
//        VBox container = new VBox(pickerA, pickerB, combine, colorContainer);
//        AnchorPane pane = new AnchorPane(container);
//        primaryStage.setScene(new Scene(pane));
//        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
