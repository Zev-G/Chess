package tmw.me.chess.game;

import tmw.me.chess.virtual.ai.AiBase;
import tmw.me.chess.virtual.ai.RandomMoveAI;
import tmw.me.chess.virtual.ai.SimpleMMAi;

public enum TeamController {
    PLAYER(null),
    RANDOM(RandomMoveAI.class),
    SIMPLEMM(SimpleMMAi.class);

    private final Class<? extends AiBase> ai;

    TeamController(Class<? extends AiBase> ai) {
        this.ai = ai;
    }

    public Class<? extends AiBase> getAi() {
        return ai;
    }
}
