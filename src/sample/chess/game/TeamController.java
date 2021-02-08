package sample.chess.game;

import sample.chess.virtual.ai.AiBase;
import sample.chess.virtual.ai.RandomMoveAI;
import sample.chess.virtual.ai.SimpleMMAi;

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
