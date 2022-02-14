package tmw.me.chess.ui.board;

public enum SpotColor {
    LIGHT,
    DARK;

    public static SpotColor fromBoardSpot(int x, int y) {
        return (x + y) % 2 == 0 ? LIGHT : DARK;
    }

}
