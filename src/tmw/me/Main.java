package tmw.me;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tmw.me.chess.ui.program.Program;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

//        Game game = new Game(true);
//        game.start();
//        game.show(primaryStage);
        primaryStage.setScene(new Scene(new Program()));
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
