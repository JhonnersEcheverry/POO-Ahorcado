package com.poo.ahorcado.config;

/**
 * Configuración del juego. Encapsula los parámetros configurables
 * que definen cómo se comporta una partida.
 */
public class GameConfig {
    private final Difficulty difficulty;
    private final int maxAttempts;
    private final int maxHints;
    
    public GameConfig(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.maxAttempts = difficulty.getMaxAttempts();
        this.maxHints = difficulty.getMaxHints();
    }
    
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    public int getMaxAttempts() {
        return maxAttempts;
    }
    
    public int getMaxHints() {
        return maxHints;
    }
}

