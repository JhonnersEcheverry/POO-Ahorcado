# Arquitectura del Proyecto Ahorcado

## 🏗️ Diseño para Fácil Integración con Swing

El proyecto está diseñado siguiendo el patrón **MVC (Model-View-Controller)** para permitir fácil intercambio entre diferentes interfaces de usuario (Consola, Swing, etc.).

## 📐 Estructura de Componentes

### 1. **Model (Modelo)**
- `Game.java` - Lógica del juego (independiente de UI)
- `WordBank.java` - Banco de palabras
- `GameState.java` - DTO que representa el estado del juego

### 2. **View (Vista)**
- `GameView.java` - **Interfaz** que define cómo interactuar con la UI
- `ConsoleGameView.java` - Implementación para consola
- `SwingGameView.java` - Implementación para Swing

### 3. **Controller (Controlador)**
- `GameController.java` - Coordina entre lógica y vista (independiente de UI)

## 🔄 Flujo de Datos

```
Usuario → GameView → GameController → Game (lógica) → GameState → GameView → Usuario
```

## ✅ Ventajas de esta Arquitectura

### 1. **Separación de Responsabilidades**
- La lógica del juego (`Game`) no conoce la UI
- El controlador (`GameController`) no depende de implementaciones específicas
- La vista (`GameView`) solo maneja interacción con el usuario

### 2. **Fácil Intercambio de UI**
```java
// Versión Consola
GameView view = new ConsoleGameView(scanner);
GameController controller = new GameController(bank, view);

// Versión Swing (mismo código de lógica!)
GameView view = new SwingGameView();
GameController controller = new GameController(bank, view);
```

### 3. **Testeable**
- Puedes crear mocks de `GameView` para tests
- La lógica se puede testear sin UI

### 4. **Escalable**
- Agregar nuevas UIs es solo implementar `GameView`
- Agregar funcionalidad solo requiere modificar `GameController`

## 📝 Para Agregar una Nueva UI

1. Implementa la interfaz `GameView`
2. Crea una clase principal que instancie tu vista
3. Usa el mismo `GameController`

Ejemplo:
```java
public class WebGameView implements GameView {
    // Implementar métodos de GameView
}

// En main:
GameView view = new WebGameView();
GameController controller = new GameController(bank, view);
controller.run();
```

## 🎯 Cómo Funciona

### GameState (DTO)
Transfiere información entre lógica y UI sin acoplamiento:
```java
GameState state = GameState.fromGame(game);
view.displayGameState(state);
```

### GameView (Interfaz)
Define un contrato que cualquier UI debe cumplir:
- `displayGameState()` - Mostrar estado
- `requestInput()` - Solicitar input
- `showMessage()` - Mostrar mensajes
- etc.

### GameController (Coordinador)
- Maneja el flujo del juego
- Procesa comandos del usuario
- Coordina entre `Game` y `GameView`
- **No depende de implementaciones específicas**

## 🚀 Uso Actual

### Consola
```bash
java com.poo.ahorcado.Ahorcado
```

### Swing (cuando esté completo)
```bash
java com.poo.ahorcado.SwingApp
```

Ambos usan la **misma lógica** y el **mismo controlador**, solo cambia la vista.

