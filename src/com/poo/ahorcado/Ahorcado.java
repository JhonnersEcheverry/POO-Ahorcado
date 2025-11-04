package com.poo.ahorcado;

import com.poo.ahorcado.logic.WordBank;
import com.poo.ahorcado.ui.GameController;
import com.poo.ahorcado.ui.console.ConsoleGameView;

import java.util.Scanner;

public class Ahorcado {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordBank bank = new WordBank();
        
        // Crear vista de consola e implementar GameView
        ConsoleGameView view = new ConsoleGameView(scanner);
        
        // Crear controlador que coordina lógica y vista
        GameController controller = new GameController(bank, view);
        controller.run();
        
        scanner.close();
    }
}

