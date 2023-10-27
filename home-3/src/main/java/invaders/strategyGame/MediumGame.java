package invaders.strategyGame;

import invaders.engine.GameEngine;

public class MediumGame implements DifficultyLevel {
    @Override
    public GameEngine loadGame() {
        return new GameEngine("src/main/resources/config_medium.json");

    }
}
