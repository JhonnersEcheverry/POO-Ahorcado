package com.poo.ahorcado.ui;

import com.poo.ahorcado.config.GameConfig;
import com.poo.ahorcado.model.WordEntry;

/**
 * Modelo de datos que representa el estado actual del juego.
 * Permite transferir información entre la lógica y la UI sin acoplamiento.
 */
public class GameState {
    private final String maskedWord;
    private final int attemptsLeft;
    private final int errors;
    private final int hintsUsed;
    private final int maxHints;
    private final boolean isFinished;
    private final boolean isWon;
    private final boolean isLost;
    private final WordEntry currentWord;
    private final GameConfig config;
    
    public GameState(String maskedWord, int attemptsLeft, int errors, 
                    int hintsUsed, int maxHints, boolean isFinished, 
                    boolean isWon, boolean isLost, WordEntry currentWord, 
                    GameConfig config) {
        this.maskedWord = maskedWord;
        this.attemptsLeft = attemptsLeft;
        this.errors = errors;
        this.hintsUsed = hintsUsed;
        this.maxHints = maxHints;
        this.isFinished = isFinished;
        this.isWon = isWon;
        this.isLost = isLost;
        this.currentWord = currentWord;
        this.config = config;
    }
    
    public String getMaskedWord() {
        return maskedWord;
    }
    
    public int getAttemptsLeft() {
        return attemptsLeft;
    }
    
    public int getErrors() {
        return errors;
    }
    
    public int getHintsUsed() {
        return hintsUsed;
    }
    
    public int getMaxHints() {
        return maxHints;
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
    public boolean isWon() {
        return isWon;
    }
    
    public boolean isLost() {
        return isLost;
    }
    
    public WordEntry getCurrentWord() {
        return currentWord;
    }
    
    public GameConfig getConfig() {
        return config;
    }
    
    /**
     * Crea un GameState a partir de un objeto Game.
     */
    public static GameState fromGame(com.poo.ahorcado.logic.Game game) {
        return new GameState(
            game.getMaskedWord(),
            game.getAttemptsLeft(),
            game.getErrors(),
            game.getHintsUsed(),
            game.getConfig().getMaxHints(),
            game.isFinished(),
            game.isWon(),
            game.isLost(),
            game.getCurrent(),
            game.getConfig()
        );
    }
}

