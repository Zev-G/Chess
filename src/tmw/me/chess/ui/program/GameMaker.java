package tmw.me.chess.ui.program;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tmw.me.chess.game.Game;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.ai.AiBase;
import tmw.me.chess.virtual.ai.LessSimpleMMAi;

public class GameMaker extends VBox {

    private static final boolean USE_QUIESCENT_SEARCH = false;

    private static final double DEFAULT_HEIGHT = 245;
    private static final double DEFAULT_WIDTH = 270;
    private static final double PADDING = 20;
    private static final double SPACING = 10;
    private static final String[] AI_NAMES = {"Player", "MM Depth: 1", "MM Depth: 2", "MM Depth: 3", "MM Depth: 4", "MM Depth: 5"};

    private final Label blackControllerLabel = genLabel("Black Controller:");
    private final DropMenu<String> blackController = new DropMenu<>(BigButton.DEFAULT_WIDTH * 0.8, BigButton.DEFAULT_HEIGHT * 0.85, BigButton.DEFAULT_COLOR.darker(), BigButton.HoverEffect.GLOW, AI_NAMES);
    private final Label whiteControllerLabel = genLabel("White Controller:");
    private final DropMenu<String> whiteController = new DropMenu<>(BigButton.DEFAULT_WIDTH * 0.8, BigButton.DEFAULT_HEIGHT * 0.85, BigButton.DEFAULT_COLOR.darker(), BigButton.HoverEffect.GLOW, AI_NAMES);

    public GameMaker() {
        getChildren().addAll(blackControllerLabel, blackController, blankRegion(30), whiteControllerLabel, whiteController);

        blackController.getSelectionModel().select(AI_NAMES[0]);
        whiteController.getSelectionModel().select(AI_NAMES[0]);

        setMinWidth(DEFAULT_WIDTH);
        setMinHeight(DEFAULT_HEIGHT);
        setMaxWidth(DEFAULT_WIDTH);

        setAlignment(Pos.TOP_CENTER);

        setPadding(new Insets(PADDING));
        setSpacing(SPACING);

        getStyleClass().add("game-maker");
    }

    public Game createGame() {
        Game game = new Game(true);
        game.setBoard(VirtualBoard.defaultBoard(game));
        AiBase blackController = null;
        AiBase whiteController = null;
        String blackText = this.blackController.getValue();
        String whiteText = this.whiteController.getValue();
        if (blackText.startsWith("MM")) {
            int depth = Integer.parseInt(String.valueOf(blackText.charAt(blackText.length() - 1)));
            blackController = new LessSimpleMMAi(depth, USE_QUIESCENT_SEARCH);
        }
        if (whiteText.startsWith("MM")) {
            int depth = Integer.parseInt(String.valueOf(whiteText.charAt(whiteText.length() - 1)));
            whiteController = new LessSimpleMMAi(depth, USE_QUIESCENT_SEARCH);
        }
        game.setBlackController(blackController);
        game.setWhiteController(whiteController);
        return game;
    }

    public static Label genLabel(String text) {
        Label label = new Label(text);
        label.setFont(BigButton.DEFAULT_SMALL_FONT);
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER_RIGHT);
        return label;
    }

    public static Region blankRegion(double height) {
        Region r = new Region();
        r.setMinHeight(height);
        return r;
    }

}
