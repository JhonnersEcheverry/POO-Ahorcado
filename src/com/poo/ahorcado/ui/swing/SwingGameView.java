package com.poo.ahorcado.ui.swing;

import com.poo.ahorcado.config.Difficulty;
import com.poo.ahorcado.model.Category;
import com.poo.ahorcado.ui.GameState;
import com.poo.ahorcado.ui.GameView;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Implementación de GameView para Swing.
 * 
 * NOTA: Esta es una implementación básica que muestra cómo integrar Swing.
 * Se puede expandir con una interfaz gráfica completa.
 */
public class SwingGameView implements GameView {
    
    private JFrame frame;
    private CompletableFuture<String> inputFuture;
    private CompletableFuture<Difficulty> difficultyFuture;
    private CompletableFuture<Optional<Category>> categoryFuture;
    private CompletableFuture<Boolean> playAgainFuture;
    
    public SwingGameView() {
        // Inicializar componentes Swing aquí
        // Por ahora, usar diálogos para mantenerlo simple
    }
    
    @Override
    public void initialize() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, 
                "¡BIENVENIDO AL AHORCADO!", 
                "Ahorcado", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    @Override
    public void cleanup() {
        if (frame != null) {
            SwingUtilities.invokeLater(() -> frame.dispose());
        }
    }
    
    @Override
    public void displayGameState(GameState state) {
        SwingUtilities.invokeLater(() -> {
            String message = String.format(
                "Palabra: %s\n" +
                "Intentos restantes: %d\n" +
                "Errores: %d\n" +
                "Pistas usadas: %d/%d",
                state.getMaskedWord(),
                state.getAttemptsLeft(),
                state.getErrors(),
                state.getHintsUsed(),
                state.getMaxHints()
            );
            JOptionPane.showMessageDialog(null, message, "Estado del Juego", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    @Override
    public Difficulty requestDifficulty() {
        difficultyFuture = new CompletableFuture<>();
        
        SwingUtilities.invokeLater(() -> {
            String[] options = {"FÁCIL", "MEDIO", "DIFÍCIL"};
            int choice = JOptionPane.showOptionDialog(null,
                "Selecciona la dificultad:",
                "Dificultad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]); // MEDIO por defecto
            
            Difficulty selected = Difficulty.MEDIO;
            if (choice == 0) selected = Difficulty.FACIL;
            else if (choice == 2) selected = Difficulty.DIFICIL;
            
            difficultyFuture.complete(selected);
        });
        
        try {
            return difficultyFuture.get();
        } catch (Exception e) {
            return Difficulty.MEDIO;
        }
    }
    
    @Override
    public Optional<Category> requestCategory() {
        categoryFuture = new CompletableFuture<>();
        
        SwingUtilities.invokeLater(() -> {
            Category[] categories = Category.values();
            String[] options = new String[categories.length + 1];
            for (int i = 0; i < categories.length; i++) {
                options[i] = categories[i].getDisplayName();
            }
            options[categories.length] = "ALEATORIA";
            
            int choice = JOptionPane.showOptionDialog(null,
                "Selecciona una categoría:",
                "Categoría",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[categories.length]); // ALEATORIA por defecto
            
            Optional<Category> selected = Optional.empty();
            if (choice >= 0 && choice < categories.length) {
                selected = Optional.of(categories[choice]);
            }
            
            categoryFuture.complete(selected);
        });
        
        try {
            return categoryFuture.get();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public String requestInput() {
        inputFuture = new CompletableFuture<>();
        
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog(null,
                "Ingresa una letra o comando:\n" +
                "1 - Categoría\n" +
                "2 - Letra aleatoria\n" +
                "3 - Pista\n" +
                "4 - Ayuda",
                "Input",
                JOptionPane.QUESTION_MESSAGE);
            
            inputFuture.complete(input != null ? input : "");
        });
        
        try {
            return inputFuture.get();
        } catch (Exception e) {
            return "";
        }
    }
    
    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    @Override
    public void showError(String error) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, error, "Error", 
                JOptionPane.ERROR_MESSAGE);
        });
    }
    
    @Override
    public void showFinalResult(GameState state) {
        SwingUtilities.invokeLater(() -> {
            String message = state.isWon() 
                ? "¡¡¡FELICIDADES!!! ¡HAS GANADO!"
                : "¡PERDISTE! Se te acabaron los intentos";
            
            message += String.format(
                "\n\nLa palabra era: %s\n" +
                "Categoría: %s\n" +
                "Errores cometidos: %d",
                state.getCurrentWord().getWord(),
                state.getCurrentWord().getCategory(),
                state.getErrors()
            );
            
            JOptionPane.showMessageDialog(null, message, "Resultado Final", 
                state.isWon() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        });
    }
    
    @Override
    public boolean askPlayAgain() {
        playAgainFuture = new CompletableFuture<>();
        
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(null,
                "¿Deseas jugar otra vez?",
                "Jugar de nuevo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            playAgainFuture.complete(choice == JOptionPane.YES_OPTION);
        });
        
        try {
            return playAgainFuture.get();
        } catch (Exception e) {
            return false;
        }
    }
}

