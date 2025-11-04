package com.poo.ahorcado;

import com.poo.ahorcado.logic.WordBank;
import com.poo.ahorcado.ui.GameController;
import com.poo.ahorcado.ui.swing.SwingGameView;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada para la aplicación Swing.
 * Muestra cómo usar el mismo GameController con una vista diferente.
 */
public class SwingApp {
    
    public static void main(String[] args) {
        // Iniciar en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            WordBank bank = new WordBank();
            
            // Crear vista Swing
            SwingGameView view = new SwingGameView();
            
            // Usar el mismo GameController que la versión de consola
            GameController controller = new GameController(bank, view);
            controller.run();
        });
    }
}

