package com.poo.ahorcado.ui;

import com.poo.ahorcado.config.Difficulty;
import com.poo.ahorcado.config.GameConfig;
import com.poo.ahorcado.logic.Game;
import com.poo.ahorcado.logic.WordBank;
import com.poo.ahorcado.model.Category;

import java.util.Optional;

/**
 * Controlador base del juego que coordina entre la lógica (Game) y la vista (GameView).
 * Esta clase es independiente de la implementación de la UI, permitiendo
 * usar consola, Swing, o cualquier otra tecnología.
 */
public class GameController {
    
    private final WordBank wordBank;
    private final GameView view;
    
    public GameController(WordBank wordBank, GameView view) {
        this.wordBank = wordBank;
        this.view = view;
    }
    
    /**
     * Inicia el bucle principal del juego.
     */
    public void run() {
        view.initialize();
        
        try {
            boolean playAgain = true;
            while (playAgain) {
                playGame();
                playAgain = view.askPlayAgain();
            }
        } finally {
            view.cleanup();
        }
    }
    
    /**
     * Ejecuta una partida del juego.
     */
    private void playGame() {
        // Solicitar configuración al usuario
        Difficulty difficulty = view.requestDifficulty();
        Optional<Category> category = view.requestCategory();
        
        // Crear juego con configuración
        GameConfig config = new GameConfig(difficulty);
        Game game = new Game(wordBank, config);
        game.startNew(category);
        
        // Mostrar información inicial
        view.showMessage("Dificultad: " + difficulty.getDisplayName());
        view.showMessage("Intentos disponibles: " + config.getMaxAttempts());
        view.showMessage("Pistas disponibles: " + config.getMaxHints());
        
        // Bucle principal del juego
        boolean shouldExit = false;
        while (!game.isFinished() && !shouldExit) {
            // Mostrar estado
            GameState state = GameState.fromGame(game);
            view.displayGameState(state);
            
            // Solicitar input del usuario
            String input = view.requestInput();
            
            if (input == null || input.trim().isEmpty()) {
                view.showError("Por favor, ingresa un comando válido.");
                continue;
            }
            
            // Procesar comando - retorna true si se debe salir
            shouldExit = processInput(game, input.trim().toUpperCase());
        }
        
        // Mostrar resultado final solo si el juego terminó normalmente (no por SALIR)
        if (!shouldExit) {
            GameState finalState = GameState.fromGame(game);
            view.showFinalResult(finalState);
        }
    }
    
    /**
     * Procesa el input del usuario y ejecuta la acción correspondiente.
     * @return true si se debe salir del juego, false para continuar
     */
    private boolean processInput(Game game, String input) {
        // Adivinar letra
        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            processLetterGuess(game, input.charAt(0));
            return false; // Continuar el juego
        }
        
        // Procesar comandos
        switch (input) {
            case "1":
            case "CATEGORIA":
            case "CAT":
                String categoryHint = game.hintCategory();
                view.showMessage("💡 " + categoryHint);
                return false;
                
            case "2":
            case "LETRA":
            case "LETTER":
                String letterHint = game.hintRandomLetter();
                view.showMessage("💡 " + letterHint);
                return false;
                
            case "3":
            case "PISTA":
            case "HINT":
                String textHint = game.hintText();
                view.showMessage("💡 " + textHint);
                return false;
                
            case "4":
            case "AYUDA":
            case "HELP":
                showHelp();
                return false;
                
            case "SALIR":
            case "EXIT":
            case "Q":
                view.showMessage("¡Juego terminado por el usuario!");
                return true; // Salir del juego
                
            default:
                view.showError("Comando no reconocido. Usa 'AYUDA' para ver los comandos disponibles.");
                return false;
        }
    }
    
    /**
     * Procesa un intento de adivinar una letra.
     */
    private void processLetterGuess(Game game, char letter) {
        GameState stateBefore = GameState.fromGame(game);
        String maskedWordBefore = stateBefore.getMaskedWord();
        int errorsBefore = stateBefore.getErrors();
        
        // Verificar si la letra ya fue revelada
        if (maskedWordBefore.indexOf(letter) != -1) {
            view.showMessage("ℹ La letra '" + letter + "' ya fue revelada anteriormente.");
            return;
        }
        
        boolean hit = game.guess(letter);
        GameState stateAfter = GameState.fromGame(game);
        int errorsAfter = stateAfter.getErrors();
        
        if (hit) {
            view.showMessage("✓ ¡Correcto! La letra '" + letter + "' está en la palabra.");
        } else {
            // Si los errores no aumentaron, significa que ya estaba en tried
            if (errorsBefore == errorsAfter && !game.isFinished()) {
                view.showMessage("ℹ Ya intentaste la letra '" + letter + "' anteriormente.");
            } else if (!game.isFinished()) {
                view.showError("✗ La letra '" + letter + "' no está en la palabra.");
                view.showMessage("  Errores: " + game.getErrors() + " / " + game.getAttemptsLeft() + " intentos restantes.");
            }
        }
    }
    
    /**
     * Muestra la ayuda del juego.
     */
    private void showHelp() {
        view.showMessage("\n╔═══════════════════════════════════╗");
        view.showMessage("║            AYUDA                   ║");
        view.showMessage("╚═══════════════════════════════════╝");
        view.showMessage("");
        view.showMessage("OBJETIVO:");
        view.showMessage("  Adivina la palabra letra por letra antes de quedarte sin intentos.");
        view.showMessage("");
        view.showMessage("REGLAS:");
        view.showMessage("  • Puedes usar hasta 3 pistas durante el juego.");
        view.showMessage("  • Si adivinas una letra incorrecta, pierdes un intento.");
        view.showMessage("");
        view.showMessage("PISTAS:");
        view.showMessage("  1. Categoría: Te muestra la categoría de la palabra.");
        view.showMessage("  2. Letra aleatoria: Revela una letra de la palabra.");
        view.showMessage("  3. Pista de texto: Te da una pista descriptiva.");
        view.showMessage("");
    }
}

