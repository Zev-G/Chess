package tmw.me.chess.virtual;

public enum Team {
    WHITE,
    BLACK;


    public int getNum() {
        return this == WHITE ? -1 : 1;
    }

    public int getAiNum() {
        return this == WHITE ? 1 : -1;
    }

    public Team opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

}
