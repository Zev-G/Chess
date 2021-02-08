package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.chess.game.Game;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.ai.extra.Benchmark;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        VirtualBoard board = VirtualBoard.defaultBoard();
        System.out.println(Benchmark.millisToCreateMoves(1000, board, board.getPieceAtLocation(1, 0)));

        Game game = new Game();
        game.show(primaryStage);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
