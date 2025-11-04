package com.poo.ahorcado.ui.console;

import com.poo.ahorcado.config.Difficulty;
import com.poo.ahorcado.model.Category;
import com.poo.ahorcado.ui.GameState;
import com.poo.ahorcado.ui.GameView;

import java.util.Optional;
import java.util.Scanner;

/**
 * Implementación de GameView para consola.
 * Maneja toda la interacción con el usuario vía terminal.
 */
public class ConsoleGameView implements GameView {
    
    private final Scanner scanner;
    
    public ConsoleGameView(Scanner scanner) {
        this.scanner = scanner;
    }
    
    @Override
    public void initialize() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║     ¡BIENVENIDO AL AHORCADO!      ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
    }
    
    @Override
    public void cleanup() {
        System.out.println("¡Gracias por jugar! ¡Hasta luego!");
    }
    
    @Override
    public void displayGameState(GameState state) {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│ Palabra: " + formatMaskedWord(state.getMaskedWord()) + " │");
        System.out.println("│ Intentos restantes: " + state.getAttemptsLeft() + "           │");
        System.out.println("│ Errores: " + state.getErrors() + "                 │");
        System.out.println("│ Pistas usadas: " + state.getHintsUsed() + "/" + state.getMaxHints() + "            │");
        System.out.println("└─────────────────────────────────┘");
        System.out.println();
    }
    
    @Override
    public Difficulty requestDifficulty() {
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
    
    @Override
    public Optional<Category> requestCategory() {
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
    
    @Override
    public String requestInput() {
        displayMenu();
        return scanner.nextLine();
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
    
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
    
    @Override
    public void showError(String error) {
        System.out.println(error);
    }
    
    @Override
    public void showFinalResult(GameState state) {
        System.out.println("\n╔═══════════════════════════════════╗");
        if (state.isWon()) {
            System.out.println("║      ¡¡¡FELICIDADES!!!           ║");
            System.out.println("║      ¡HAS GANADO!                ║");
        } else {
            System.out.println("║      ¡PERDISTE!                  ║");
            System.out.println("║      Se te acabaron los intentos ║");
        }
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
        System.out.println("La palabra era: " + state.getCurrentWord().getWord());
        System.out.println("Categoría: " + state.getCurrentWord().getCategory());
        System.out.println("Errores cometidos: " + state.getErrors());
        System.out.println();
    }
    
    @Override
    public boolean askPlayAgain() {
        System.out.print("\n¿Deseas jugar otra vez? (s/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("s") || response.equals("si") || response.equals("y") || response.equals("yes");
    }
    
    private String formatMaskedWord(String masked) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < masked.length(); i++) {
            sb.append(masked.charAt(i));
            if (i < masked.length() - 1) {
                sb.append(" ");
            }
        }
        String result = sb.toString();
        if (result.length() > 20) {
            return result.substring(0, 17) + "...";
        }
        return String.format("%-20s", result);
    }
}

