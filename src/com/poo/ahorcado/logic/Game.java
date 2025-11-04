package com.poo.ahorcado.logic;

import com.poo.ahorcado.config.GameConfig;
import com.poo.ahorcado.model.Category;
import com.poo.ahorcado.model.WordEntry;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Game {
    private final WordBank bank;
    private final GameConfig config;

    // Estado de la partida
    private WordEntry current;
    private char[] masked;               // letras visibles/ocultas
    private Set<Character> tried;        // letras ya intentadas
    private int errors;                  // errores acumulados
    private int hintsUsed;               // pistas usadas
    private boolean usedCatHint, usedLetterHint, usedTextHint;

    public Game(WordBank bank, GameConfig config) {
        this.bank = bank;
        this.config = config;
    }

    public void startNew(Optional<Category> cat) {
        current = cat.isPresent() ? bank.randomByCategory(cat.get()) : bank.randomAny();
        String w = current.getWord();
        masked = new char[w.length()];
        for (int i = 0; i < w.length(); i++) masked[i] = (w.charAt(i) == ' ') ? ' ' : '_';
        tried = new HashSet<>();
        errors = 0;
        hintsUsed = 0;
        usedCatHint = usedLetterHint = usedTextHint = false;
    }

    public boolean guess(char raw) {
        char c = Character.toUpperCase(raw);
        if (tried.contains(c) || isFinished()) return false; // ya intentada o juego terminado
        tried.add(c);

        boolean hit = false;
        String w = current.getWord();
        for (int i = 0; i < w.length(); i++) {
            if (w.charAt(i) == c) { masked[i] = c; hit = true; }
        }
        if (!hit) errors++;
        return hit;
    }

    // ---- Pistas ----
    public String hintCategory() {
        if (usedCatHint || !canUseMoreHints()) return "No disponible.";
        usedCatHint = true; hintsUsed++;
        return "Categoría: " + current.getCategory();
    }

    public String hintRandomLetter() {
        if (usedLetterHint || !canUseMoreHints()) return "No disponible.";
        // buscar índice no revelado
        int idx = -1;
        for (int i = 0; i < masked.length; i++) {
            if (masked[i] == '_') { idx = i; break; }
        }
        if (idx == -1) return "Todas las letras están reveladas.";
        char c = current.getWord().charAt(idx);
        guess(c); // reutiliza lógica para revelar
        usedLetterHint = true; hintsUsed++;
        return "Letra revelada: " + c;
    }

    public String hintText() {
        if (usedTextHint || !canUseMoreHints()) return "No disponible.";
        usedTextHint = true; hintsUsed++;
        return "Pista: " + current.getTextHint();
    }

    private boolean canUseMoreHints() { 
        return hintsUsed < config.getMaxHints(); 
    }

    // ---- Consultas ----
    public String getMaskedWord() { return new String(masked); }
    public int getAttemptsLeft() { return config.getMaxAttempts() - errors; }
    public int getErrors() { return errors; }
    public boolean isWon() { return current != null && current.getWord().equals(getMaskedWord()); }
    public boolean isLost() { return errors >= config.getMaxAttempts(); }
    public boolean isFinished() { return isWon() || isLost(); }
    public WordEntry getCurrent() { return current; }
    public GameConfig getConfig() { return config; }
    public int getHintsUsed() { return hintsUsed; }
}
