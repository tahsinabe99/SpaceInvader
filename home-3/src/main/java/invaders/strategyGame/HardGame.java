package invaders.strategyGame;

import invaders.engine.GameEngine;

public class HardGame implements DifficultyLevel {
    @Override
    public GameEngine loadGame() {
        return new GameEngine("src/main/resources/config_hard.json");

    }
}
