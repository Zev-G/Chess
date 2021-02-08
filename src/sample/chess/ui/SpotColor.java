package sample.chess.ui;

public enum SpotColor {
    LIGHT,
    DARK;

    public static SpotColor fromBoardSpot(int x, int y) {
        return (x + y) % 2 == 0 ? LIGHT : DARK;
    }

}
