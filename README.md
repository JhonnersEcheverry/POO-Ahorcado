# 🎮 Ahorcado - Juego del Ahorcado en Java

**Versión 0.0.1**

Un juego del ahorcado implementado en Java con arquitectura MVC **UI-agnostic**, diseñado para ser fácilmente extensible con diferentes interfaces de usuario (Consola, Swing, Web, etc.).

## 📋 Características

- ✅ **Sistema de dificultades**: Fácil, Medio y Difícil (configurables)
- ✅ **Categorías dinámicas**: Cargadas desde configuración
- ✅ **Sistema de pistas**: 3 tipos de pistas disponibles
- ✅ **Configuración externa**: Palabras en JSON, categorías en Properties
- ✅ **Arquitectura MVC**: Separación clara entre lógica y UI
- ✅ **UI-Agnostic**: La lógica no depende de ninguna tecnología de interfaz específica
- ✅ **Procesamiento JSON**: Usa Gson para parsing robusto
- ✅ **Configuración centralizada**: Categorías gestionadas desde archivo de propiedades

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Model-View-Controller)** con diseño **UI-agnostic**, permitiendo intercambiar interfaces sin modificar la lógica del juego.

```
┌─────────────────────────────────────────┐
│         LÓGICA (UI-Agnostic)            │
│  ┌──────────┐      ┌──────────┐        │
│  │   Game   │      │ WordBank │        │
│  │ (Lógica) │      │ (Datos)  │        │
│  └─────┬────┘      └────┬─────┘        │
└────────┼─────────────────┼──────────────┘
         │                 │
         └─────────┬───────┘
                   │
         ┌─────────▼─────────┐
         │  GameController   │ ← Coordinador (UI-agnostic)
         │  (No conoce UI)   │
         └─────────┬─────────┘
                   │ usa
         ┌─────────▼─────────┐
         │   GameView        │ ← Interfaz (Contrato)
         │   (Interface)      │
         └────┬──────┬───────┘
              │      │ implementa
    ┌─────────┘      └─────────┐
    │                          │
┌───▼────────┐        ┌───────▼──────┐
│ Console    │        │ Swing        │
│ GameView   │        │ GameView     │
│ (Scanner)  │        │ (JFrame)     │
└────────────┘        └──────────────┘
```

### 🎯 ¿Qué significa UI-Agnostic?

**UI-Agnostic** significa que la lógica del juego **no sabe ni le importa** qué tecnología de interfaz se está usando. Puede funcionar con:
- ✅ Consola (Scanner)
- ✅ Swing (JFrame)
- ✅ JavaFX
- ✅ Web (REST API)
- ✅ Cualquier otra UI que implemente `GameView`

**Ventaja:** Cambiar de UI solo requiere implementar `GameView`. La lógica permanece igual.

Para más detalles, consulta [ARQUITECTURA.md](ARQUITECTURA.md).

## 📁 Estructura del Proyecto

```
Ahorcado/
├── lib/
│   └── gson-2.10.1.jar          # Dependencia: Gson para JSON
├── src/
│   ├── com/poo/ahorcado/
│   │   ├── Ahorcado.java              # Punto de entrada (Consola)
│   │   ├── SwingApp.java              # Punto de entrada (Swing)
│   │   ├── config/
│   │   │   ├── CategoryManager.java   # Gestor de categorías (Singleton)
│   │   │   ├── Difficulty.java        # Niveles de dificultad (Enum)
│   │   │   └── GameConfig.java        # Configuración del juego
│   │   ├── data/
│   │   │   ├── WordLoader.java        # Carga palabras desde JSON (Gson)
│   │   │   └── WordsData.java         # DTO para mapeo JSON
│   │   ├── logic/
│   │   │   ├── Game.java              # Lógica del juego (UI-agnostic)
│   │   │   └── WordBank.java          # Banco de palabras
│   │   ├── model/
│   │   │   ├── Category.java          # Clase de categorías (dinámica)
│   │   │   └── WordEntry.java         # Modelo de palabra
│   │   └── ui/
│   │       ├── GameView.java          # Interfaz de UI (contrato)
│   │       ├── GameState.java         # DTO de estado
│   │       ├── GameController.java    # Controlador (UI-agnostic)
│   │       ├── console/
│   │       │   └── ConsoleGameView.java
│   │       └── swing/
│   │           └── SwingGameView.java
│   ├── resources/
│   │   ├── words.json                 # Palabras del juego
│   │   └── categories.properties      # Configuración de categorías
│   └── game.properties                # Configuración general
└── build/
    └── classes/                       # Archivos compilados
```

## 🚀 Compilación y Ejecución

### Requisitos

- **Java 24** o superior
- **Gson 2.10.1** (incluido en `lib/`)
- **Ant** (incluido en NetBeans) o compilador Java

### Configurar Dependencias

1. Descarga **Gson** desde: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
2. Coloca el JAR en la carpeta `lib/` en la raíz del proyecto
3. En NetBeans: Properties → Libraries → Add JAR/Folder → selecciona `lib/gson-2.10.1.jar`

### Compilar

**Windows PowerShell:**
```powershell
# Compilar con Gson en el classpath
$files = Get-ChildItem -Path src/com/poo/ahorcado -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -cp "lib/gson-2.10.1.jar" -d build/classes -sourcepath src $files

# Copiar recursos
Copy-Item -Path "src/resources" -Destination "build/classes/resources" -Recurse -Force
```

**Linux/Mac:**
```bash
javac -cp "lib/gson-2.10.1.jar" -d build/classes -sourcepath src src/com/poo/ahorcado/**/*.java
cp -r src/resources build/classes/
```

### Ejecutar

**Versión Consola:**
```bash
java -cp "build/classes;lib/gson-2.10.1.jar" com.poo.ahorcado.Ahorcado
```

**Versión Swing:**
```bash
java -cp "build/classes;lib/gson-2.10.1.jar" com.poo.ahorcado.SwingApp
```

**Nota:** En Linux/Mac, usa `:` en lugar de `;` en el classpath.

### Usando NetBeans

1. Abre el proyecto en NetBeans
2. Configura Gson: Properties → Libraries → Add JAR/Folder → `lib/gson-2.10.1.jar`
3. Click derecho en el proyecto → "Clean and Build"
4. Click derecho en `Ahorcado.java` → "Run File" (Consola)
5. Click derecho en `SwingApp.java` → "Run File" (Swing)

## 🎮 Cómo Jugar

### Inicio

1. Selecciona la **dificultad**:
   - **Fácil**: 10 intentos, 5 pistas
   - **Medio**: 7 intentos, 3 pistas
   - **Difícil**: 5 intentos, 2 pistas

2. Selecciona una **categoría** o elige "Aleatoria"

3. ¡Comienza a adivinar!

### Comandos

- **Letra**: Escribe una letra para adivinar
- **1 o CATEGORIA**: Muestra la categoría de la palabra
- **2 o LETRA**: Revela una letra aleatoria
- **3 o PISTA**: Muestra una pista descriptiva
- **4 o AYUDA**: Muestra ayuda del juego
- **SALIR**: Termina el juego

### Objetivo

Adivina la palabra letra por letra antes de quedarte sin intentos.

## ⚙️ Configuración

### Agregar Palabras

Edita `src/resources/words.json`:

```json
{
  "words": [
    {
      "word": "NUEVA_PALABRA",
      "category": "CATEGORIA",
      "hint": "Pista descriptiva"
    }
  ]
}
```

**Categorías disponibles** (configuradas en `categories.properties`):
- `ANIMALES`
- `PAISES`
- `DEPORTES`
- `OBJETOS`
- `COMIDAS`

### Configurar Categorías (Sin Recompilar)

Las categorías son completamente dinámicas. Solo necesitas editar el archivo de configuración:

Edita `src/resources/categories.properties`:

```properties
# Lista de categorías separadas por comas
categories=ANIMALES,PAISES,DEPORTES,OBJETOS,COMIDAS,CIENCIA

# Nombres de visualización (opcional)
category.ANIMALES.display=Animales
category.PAISES.display=Países
category.CIENCIA.display=Ciencia
```

**Ventajas:**
- ✅ **No necesitas recompilar** el código
- ✅ **No necesitas redesplegar** la aplicación
- ✅ Solo edita el archivo y reinicia el juego
- ✅ Las categorías se cargan automáticamente

**Pasos para agregar una nueva categoría:**
1. Edita `categories.properties` y agrega la categoría a la lista
2. Agrega palabras con esa categoría en `words.json`
3. Reinicia el juego → ¡Listo!

### Modificar Dificultades

Edita `src/com/poo/ahorcado/config/Difficulty.java`:

```java
public enum Difficulty {
    FACIL(10, 5, "Fácil"),      // intentos, pistas, nombre
    MEDIO(7, 3, "Medio"),
    DIFICIL(5, 2, "Difícil");
    // ...
}
```

### Configuración General

Edita `src/game.properties` para configuraciones adicionales.

## 🎯 Niveles de Dificultad

| Dificultad | Intentos | Pistas | Descripción |
|-----------|----------|--------|-------------|
| **Fácil** | 10 | 5 | Para principiantes |
| **Medio** | 7 | 3 | Desafío moderado |
| **Difícil** | 5 | 2 | Para expertos |

## 🔧 Tecnologías y Patrones

- **Lenguaje**: Java 24
- **Librerías**: Gson 2.10.1 (procesamiento JSON)
- **Patrón**: MVC (Model-View-Controller)
- **Arquitectura**: UI-Agnostic (independiente de tecnología de interfaz)
- **Configuración**: JSON (palabras) + Properties (categorías y config)
- **UI**: Consola (funcional) + Swing (diálogos básicos)

## 📚 Conceptos Implementados

### Buenas Prácticas

- ✅ **Separación de responsabilidades** (SRP)
- ✅ **Inversión de dependencias** (DIP) - GameController depende de GameView (interfaz)
- ✅ **Open/Closed Principle** (extensible sin modificar)
- ✅ **Configuration externalization** (sin valores hardcodeados)
- ✅ **Testeable** (componentes desacoplados)
- ✅ **UI-Agnostic Design** (lógica independiente de UI)

### Patrones de Diseño

- **MVC**: Model-View-Controller
- **Strategy**: Intercambio de vistas (GameView)
- **DTO**: GameState y WordsData para transferencia de datos
- **Factory**: WordLoader para crear WordEntry
- **Singleton**: CategoryManager para gestión centralizada
- **Adapter**: Cada implementación de GameView adapta su UI al contrato

## ✨ Características Técnicas

- ✅ **Gson Integration**: Procesamiento JSON robusto con Gson
- ✅ **CategoryManager**: Configuración centralizada de categorías
- ✅ **Categorías Dinámicas**: Category como clase dinámica cargada desde configuración
- ✅ **Sin Recompilación**: Agregar categorías solo requiere editar configuración
- ✅ **Validación**: Las categorías se validan contra configuración
- ✅ **Manejo de Errores**: Validación y mensajes de error claros

## 🚧 Próximos Pasos

- [ ] Interfaz gráfica completa con Swing (ventana con componentes)
- [ ] Sistema de puntuación
- [ ] Estadísticas de partidas
- [ ] Modo multijugador
- [ ] Persistencia de configuración
- [ ] Tests unitarios
- [ ] Limpieza de código no utilizado

## 👥 Contribuir

Este es un proyecto educativo. Siéntete libre de:
- Agregar más palabras al JSON
- Agregar nuevas categorías (solo edita `categories.properties`)
- Mejorar la interfaz de consola
- Implementar la UI de Swing completa
- Crear nuevas implementaciones de `GameView` (JavaFX, Web, etc.)
- Agregar tests unitarios
- Mejorar la documentación

## 🎯 Agregar una Nueva UI

¿Quieres agregar una nueva interfaz? Solo implementa `GameView`:

```java
public class JavaFXGameView implements GameView {
    // Implementar todos los métodos de GameView
    // Usar componentes de JavaFX
}

// Uso:
GameView view = new JavaFXGameView();
GameController controller = new GameController(bank, view);
controller.run();  // ← Mismo código, nueva UI!
```

## 📝 Licencia

Proyecto educativo - Uso libre para fines educativos.

## 🐛 Problemas Conocidos

- La implementación Swing actual es básica (usa diálogos JOptionPane)
- No hay persistencia de datos entre sesiones
- No hay sistema de puntuación
- Algunos archivos no utilizados (pendiente de limpieza)

## 📖 Documentación Adicional

- [ARQUITECTURA.md](ARQUITECTURA.md) - Detalles de la arquitectura MVC y UI-agnostic

---

**Desarrollado con ❤️ para aprender POO, buenas prácticas y arquitectura UI-agnostic**
