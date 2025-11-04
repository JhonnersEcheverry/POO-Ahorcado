package com.poo.ahorcado.ui.console;

import com.poo.ahorcado.config.Difficulty;
import com.poo.ahorcado.config.GameConfig;
import com.poo.ahorcado.logic.Game;
import com.poo.ahorcado.logic.WordBank;
import com.poo.ahorcado.model.Category;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleGameController {
    
    private final Scanner scanner;
    private final WordBank bank;
    
    public ConsoleGameController(Scanner scanner, WordBank bank) {
        this.scanner = scanner;
        this.bank = bank;
    }
    
    public void run() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║     ¡BIENVENIDO AL AHORCADO!      ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
        
        boolean playAgain = true;
        while (playAgain) {
            playGame();
            System.out.print("\n¿Deseas jugar otra vez? (s/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            playAgain = response.equals("s") || response.equals("si") || response.equals("y") || response.equals("yes");
            System.out.println();
        }
        
        System.out.println("¡Gracias por jugar! ¡Hasta luego!");
    }
    
    private void playGame() {
        // Seleccionar dificultad
        Difficulty difficulty = chooseDifficulty();
        GameConfig config = new GameConfig(difficulty);
        Game game = new Game(bank, config);
        
        // Seleccionar categoría
        Optional<Category> selectedCategory = chooseCategory();
        game.startNew(selectedCategory);
        
        System.out.println("Dificultad seleccionada: " + difficulty.getDisplayName());
        System.out.println("Intentos disponibles: " + config.getMaxAttempts());
        System.out.println("Pistas disponibles: " + config.getMaxHints());
        
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║         ¡COMIENZA EL JUEGO!       ║");
        System.out.println("╚═══════════════════════════════════╝\n");
        
        // Bucle principal del juego
        while (!game.isFinished()) {
            displayGameState(game);
            displayMenu();
            
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.isEmpty()) {
                System.out.println("Por favor, ingresa un comando válido.\n");
                continue;
            }
            
            // Procesar comando
            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                processLetterGuess(game, input.charAt(0));
            } else if (input.equals("1") || input.equals("CATEGORIA") || input.equals("CAT")) {
                // Pista de categoría
                System.out.println("💡 " + game.hintCategory() + "\n");
            } else if (input.equals("2") || input.equals("LETRA") || input.equals("LETTER")) {
                // Pista de letra aleatoria
                System.out.println("💡 " + game.hintRandomLetter() + "\n");
            } else if (input.equals("3") || input.equals("PISTA") || input.equals("HINT")) {
                // Pista de texto
                System.out.println("💡 " + game.hintText() + "\n");
            } else if (input.equals("4") || input.equals("AYUDA") || input.equals("HELP")) {
                // Mostrar ayuda
                showHelp();
            } else if (input.equals("SALIR") || input.equals("EXIT") || input.equals("Q")) {
                System.out.println("¡Juego terminado por el usuario!");
                return;
            } else {
                System.out.println("Comando no reconocido. Usa 'AYUDA' para ver los comandos disponibles.\n");
            }
        }
        
        // Resultado final
        displayFinalResult(game);
    }
    
    private void processLetterGuess(Game game, char letter) {
        String maskedWordBefore = game.getMaskedWord();
        int errorsBefore = game.getErrors();
        
        // Verificar si la letra ya fue revelada (visible en la palabra)
        if (maskedWordBefore.indexOf(letter) != -1) {
            System.out.println("ℹ La letra '" + letter + "' ya fue revelada anteriormente.\n");
            return;
        }
        
        boolean hit = game.guess(letter);
        int errorsAfter = game.getErrors();
        
        if (hit) {
            System.out.println("✓ ¡Correcto! La letra '" + letter + "' está en la palabra.\n");
        } else {
            // Si los errores no aumentaron, significa que ya estaba en tried (no se procesó)
            if (errorsBefore == errorsAfter && !game.isFinished()) {
                System.out.println("ℹ Ya intentaste la letra '" + letter + "' anteriormente.\n");
            } else if (!game.isFinished()) {
                // Los errores aumentaron, fue un intento fallido
                System.out.println("✗ La letra '" + letter + "' no está en la palabra.");
                System.out.println("  Errores: " + game.getErrors() + " / " + game.getAttemptsLeft() + " intentos restantes.\n");
            }
        }
    }
    
    private Difficulty chooseDifficulty() {
        System.out.println("\nSelecciona la dificultad:");
        System.out.println("  1. FÁCIL   - " + Difficulty.FACIL.getMaxAttempts() + " intentos, " + Difficulty.FACIL.getMaxHints() + " pistas");
        System.out.println("  2. MEDIO   - " + Difficulty.MEDIO.getMaxAttempts() + " intentos, " + Difficulty.MEDIO.getMaxHints() + " pistas");
        System.out.println("  3. DIFÍCIL - " + Difficulty.DIFICIL.getMaxAttempts() + " intentos, " + Difficulty.DIFICIL.getMaxHints() + " pistas");
        System.out.print("\nOpción (1-3, Enter para MEDIO): ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                return Difficulty.FACIL;
            case "3":
                return Difficulty.DIFICIL;
            case "2":
            case "":
            default:
                return Difficulty.MEDIO;
        }
    }
    
    private Optional<Category> chooseCategory() {
        Category[] categories = Category.values();
        
        System.out.println("Selecciona una categoría:");
        for (int i = 0; i < categories.length; i++) {
            System.out.println("  " + (i + 1) + ". " + categories[i].getDisplayName());
        }
        System.out.println("  " + (categories.length + 1) + ". ALEATORIA (cualquier categoría)");
        System.out.print("\nOpción (1-" + (categories.length + 1) + "): ");
        
        String choice = scanner.nextLine().trim();
        
        try {
            int option = Integer.parseInt(choice);
            if (option >= 1 && option <= categories.length) {
                return Optional.of(categories[option - 1]);
            } else if (option == categories.length + 1) {
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            // Invalid input, will default to empty
        }
        
        return Optional.empty();
    }
    
    private void displayGameState(Game game) {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│ Palabra: " + formatMaskedWord(game.getMaskedWord()) + " │");
        System.out.println("│ Intentos restantes: " + game.getAttemptsLeft() + "           │");
        System.out.println("│ Errores: " + game.getErrors() + "                 │");
        System.out.println("│ Pistas usadas: " + game.getHintsUsed() + "/" + game.getConfig().getMaxHints() + "            │");
        System.out.println("└─────────────────────────────────┘");
        System.out.println();
    }
    
    
    private String formatMaskedWord(String masked) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < masked.length(); i++) {
            sb.append(masked.charAt(i));
            if (i < masked.length() - 1) {
                sb.append(" ");
            }
        }
        // Ajustar para que quepa en el formato
        String result = sb.toString();
        if (result.length() > 20) {
            return result.substring(0, 17) + "...";
        }
        return String.format("%-20s", result);
    }
    
    private void displayMenu() {
        System.out.println("Comandos disponibles:");
        System.out.println("  • Escribe una LETRA para adivinar");
        System.out.println("  • 1 o CATEGORIA - Pista de categoría");
        System.out.println("  • 2 o LETRA - Revelar una letra aleatoria");
        System.out.println("  • 3 o PISTA - Mostrar pista de texto");
        System.out.println("  • 4 o AYUDA - Mostrar esta ayuda");
        System.out.println("  • SALIR - Terminar el juego");
        System.out.print("\nTu elección: ");
    }
    
    private void showHelp() {
        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║            AYUDA                   ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
        System.out.println("OBJETIVO:");
        System.out.println("  Adivina la palabra letra por letra antes de quedarte sin intentos.");
        System.out.println();
        System.out.println("REGLAS:");
        System.out.println("  • Tienes 7 intentos para adivinar la palabra.");
        System.out.println("  • Puedes usar hasta 3 pistas durante el juego.");
        System.out.println("  • Si adivinas una letra incorrecta, pierdes un intento.");
        System.out.println();
        System.out.println("PISTAS:");
        System.out.println("  1. Categoría: Te muestra la categoría de la palabra.");
        System.out.println("  2. Letra aleatoria: Revela una letra de la palabra.");
        System.out.println("  3. Pista de texto: Te da una pista descriptiva.");
        System.out.println();
    }
    
    private void displayFinalResult(Game game) {
        System.out.println("\n╔═══════════════════════════════════╗");
        if (game.isWon()) {
            System.out.println("║      ¡¡¡FELICIDADES!!!           ║");
            System.out.println("║      ¡HAS GANADO!                ║");
        } else {
            System.out.println("║      ¡PERDISTE!                  ║");
            System.out.println("║      Se te acabaron los intentos ║");
        }
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
        System.out.println("La palabra era: " + game.getCurrent().getWord());
        System.out.println("Categoría: " + game.getCurrent().getCategory());
        System.out.println("Errores cometidos: " + game.getErrors());
        System.out.println();
    }
}

