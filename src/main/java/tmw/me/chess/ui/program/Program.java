package tmw.me.chess.ui.program;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import tmw.me.chess.game.Game;
import tmw.me.chess.ui.SVG;
import tmw.me.chess.ui.board.Board;
import tmw.me.chess.ui.game.GameContainer;
import tmw.me.chess.ui.Styles;

import java.util.function.Consumer;

public class Program extends AnchorPane {

    private final BigButton playButton = new BigButton("New Game");
    private final BigButton fromFenButton = new BigButton("From FEN");

    private final SVGPath homeButton = Board.fromPath(SVG.resizePath(SVG.HOME_ICON, 2));

    private final VBox buttons = new VBox(playButton, fromFenButton);
    private final BorderPane middleBox = new BorderPane();
    private final BorderPane topPane = new BorderPane();
    private final VBox layout = new VBox(middleBox);

    private GameContainer currentGame;

    public Program() {
        getStyleClass().add("program");
        topPane.getStyleClass().add("top-pane");
        getStylesheets().add(Styles.get("board"));

        BorderPane homeButtonHolder = new BorderPane();
        homeButtonHolder.setCenter(homeButton);

        getChildren().addAll(layout, topPane);
        middleBox.setCenter(buttons);
        topPane.setLeft(homeButtonHolder);

        buttons.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);
        buttons.setSpacing(25);
        topPane.setMinHeight(45);
        topPane.setMaxHeight(45);
        homeButton.setOpacity(0.3);
        homeButtonHolder.setPadding(new Insets(5));

        AnchorPane.setTopAnchor(layout, 45D);
        AnchorPane.setBottomAnchor(layout, 0D);
        AnchorPane.setRightAnchor(layout, 0D);
        AnchorPane.setLeftAnchor(layout, 0D);

        AnchorPane.setTopAnchor(topPane, 0D);
        AnchorPane.setRightAnchor(topPane, 0D);
        AnchorPane.setLeftAnchor(topPane, 0D);
        var ref = new Object() {
            EventHandler<MouseEvent> mousePressedHandler = null;
        };
        ref.mousePressedHandler = mouseEvent -> {
            playButton.setText("Start Game");
            buttons.getChildren().remove(fromFenButton);
            GameMaker gameMaker = new GameMaker();
            buttons.getChildren().add(gameMaker);
            BigButton backButton = new BigButton("<- Back");
            playButton.setOnMousePressed(mouseEvent1 -> {
                Game game = gameMaker.createGame();
                backButton.fire();
                switchViewToGame(game);
                game.start();
            });
            buttons.getChildren().add(backButton);
            backButton.setOnAction(mouseEvent1 -> {
                playButton.setText("New Game");
                playButton.setOnMousePressed(ref.mousePressedHandler);
                buttons.getChildren().removeAll(backButton, gameMaker);
                buttons.getChildren().add(fromFenButton);
            });
        };
        playButton.setOnMousePressed(ref.mousePressedHandler);

        var ref1 = new Object() {
            EventHandler<MouseEvent> mousePressedHandler = null;
        };
        ref1.mousePressedHandler = mouseEvent -> {
            fromFenButton.setText("Start Game");
            buttons.getChildren().remove(playButton);
            TextField field = new TextField(Game.DEFAULT_BOARD);
            buttons.getChildren().add(field);
            Background ogBg = field.getBackground();
            Effect ogShadow = field.getEffect();
            GameMaker gameMaker = new GameMaker();
            buttons.getChildren().add(gameMaker);
            BigButton backButton = new BigButton("<- Back");
            fromFenButton.setOnMousePressed(mouseEvent1 -> {
                try {
                    Game game = Game.fromFen(field.getText(), true);
                    Game gameMakerGame = gameMaker.createGame();
                    game.setWhiteController(gameMakerGame.getWhiteController());
                    game.setBlackController(gameMakerGame.getBlackController());
                    backButton.fire();
                    switchViewToGame(game);
                    game.start();
                } catch (Exception ignore) {
                    field.setBackground(new Background(new BackgroundFill(Color.RED.darker(), new CornerRadii(BigButton.CORNER_RADII), Insets.EMPTY)));
                    DropShadow newShadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.RED.darker().darker(), field.getMinHeight() / 10, 1, 0, field.getMinHeight() / 15);
                    newShadow.setWidth(0);
                    field.setEffect(newShadow);
                    field.textProperty().addListener((observableValue, s, t1) -> {
                        field.setBackground(ogBg);
                        field.setEffect(ogShadow);
                        field.getDefaultEffect().set(ogShadow);
                    });
                }
            });
            buttons.getChildren().add(backButton);
            backButton.setOnAction(mouseEvent1 -> {
                fromFenButton.setText("From FEN");
                fromFenButton.setOnMousePressed(ref1.mousePressedHandler);
                buttons.getChildren().removeAll(backButton, field, gameMaker);
                buttons.getChildren().add(0, playButton);
            });
        };
        fromFenButton.setOnMousePressed(ref1.mousePressedHandler);

        homeButton.setOnMousePressed(mouseEvent -> switchToHomeView());
    }

    public void switchViewToGame(Game game) {
        if (getScene() != null) {
            game.applyWithScene(getScene());
        }
        layout.getChildren().remove(middleBox);
        currentGame = new GameContainer(game);
        Consumer<Number> updateWidth = number -> currentGame.setMinWidth(number.doubleValue() + currentGame.getBoundsInScene().getMinX());
        Consumer<Number> updateHeight = number -> currentGame.setMinHeight(number.doubleValue() + currentGame.getBoundsInScene().getMinY() - 10);
        layout.widthProperty().addListener((observableValue, number, t1) -> updateWidth.accept(t1));
        layout.heightProperty().addListener((observableValue, number, t1) -> updateHeight.accept(t1));
        updateHeight.accept(getHeight() - 10);
        updateWidth.accept(getWidth());
        layout.getChildren().add(currentGame);
    }

    public void switchToHomeView() {
        if (currentGame != null) {
            currentGame.getGame().stop();
        } else {
            return;
        }
        layout.getChildren().remove(currentGame);
        layout.getChildren().add(middleBox);
        currentGame = null;
    }

}
