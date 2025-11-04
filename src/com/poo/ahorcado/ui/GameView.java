package com.poo.ahorcado.ui;

import com.poo.ahorcado.config.Difficulty;
import com.poo.ahorcado.model.Category;

/**
 * Interfaz para la vista del juego.
 * Permite desacoplar la lógica del juego de la implementación de la UI
 * (Consola, Swing, etc.).
 */
public interface GameView {
    
    /**
     * Muestra el estado actual del juego.
     */
    void displayGameState(GameState state);
    
    /**
     * Solicita al usuario que seleccione una dificultad.
     */
    Difficulty requestDifficulty();
    
    /**
     * Solicita al usuario que seleccione una categoría.
     * @return Optional con la categoría seleccionada, o empty para aleatoria
     */
    java.util.Optional<Category> requestCategory();
    
    /**
     * Solicita al usuario que ingrese una letra o comando.
     */
    String requestInput();
    
    /**
     * Muestra un mensaje informativo.
     */
    void showMessage(String message);
    
    /**
     * Muestra un mensaje de error.
     */
    void showError(String error);
    
    /**
     * Muestra el resultado final del juego.
     */
    void showFinalResult(GameState state);
    
    /**
     * Pregunta al usuario si desea jugar otra vez.
     */
    boolean askPlayAgain();
    
    /**
     * Inicializa la vista (mostrar bienvenida, etc.).
     */
    void initialize();
    
    /**
     * Limpia/cierra la vista.
     */
    void cleanup();
}

