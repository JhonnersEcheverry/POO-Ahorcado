package com.poo.ahorcado.config;

/**
 * Representa los niveles de dificultad del juego.
 * Cada dificultad define el número máximo de intentos y pistas disponibles.
 */
public enum Difficulty {
    FACIL(10, 5, "Fácil"),
    MEDIO(7, 3, "Medio"),
    DIFICIL(5, 2, "Difícil");
    
    private final int maxAttempts;
    private final int maxHints;
    private final String displayName;
    
    Difficulty(int maxAttempts, int maxHints, String displayName) {
        this.maxAttempts = maxAttempts;
        this.maxHints = maxHints;
        this.displayName = displayName;
    }
    
    public int getMaxAttempts() {
        return maxAttempts;
    }
    
    public int getMaxHints() {
        return maxHints;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Obtiene la dificultad por defecto.
     */
    public static Difficulty getDefault() {
        return MEDIO;
    }
}

