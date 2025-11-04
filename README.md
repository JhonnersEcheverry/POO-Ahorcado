# 🎮 Ahorcado - Juego del Ahorcado en Java

Un juego del ahorcado implementado en Java con arquitectura MVC, diseñado para ser fácilmente extensible con diferentes interfaces de usuario (Consola y Swing).

## 📋 Características

- ✅ **Sistema de dificultades**: Fácil, Medio y Difícil (configurables)
- ✅ **Múltiples categorías**: Animales, Países, Deportes, Objetos, Comidas (configurables)
- ✅ **Sistema de pistas**: 3 tipos de pistas disponibles
- ✅ **Configuración externa**: Palabras en JSON, categorías en Properties
- ✅ **Arquitectura MVC**: Separación clara entre lógica y UI
- ✅ **Procesamiento JSON**: Usa Gson para parsing robusto
- ✅ **Configuración centralizada**: Categorías gestionadas desde archivo de propiedades

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Model-View-Controller)** para permitir fácil intercambio entre diferentes interfaces de usuario.

```
┌─────────────┐
│   GameView  │ ← Interfaz (Contrato)
│  (Interface)│
└──────┬──────┘
       │ implementa
       ├─── ConsoleGameView (Consola)
       └─── SwingGameView (Swing)
              │
              ▼
┌──────────────────┐      ┌──────────┐
│ GameController   │─────▶│   Game   │
│  (Coordina)      │      │ (Lógica) │
└──────────────────┘      └──────────┘
```

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
│   │   │   ├── CategoryManager.java   # Gestor de categorías (configuración)
│   │   │   ├── Difficulty.java        # Niveles de dificultad
│   │   │   └── GameConfig.java        # Configuración del juego
│   │   ├── data/
│   │   │   ├── WordLoader.java        # Carga palabras desde JSON (Gson)
│   │   │   └── WordsData.java         # DTO para mapeo JSON
│   │   ├── logic/
│   │   │   ├── Game.java              # Lógica del juego
│   │   │   └── WordBank.java          # Banco de palabras
│   │   ├── model/
│   │   │   ├── Category.java          # Enum de categorías
│   │   │   └── WordEntry.java         # Modelo de palabra
│   │   └── ui/
│   │       ├── GameView.java          # Interfaz de UI
│   │       ├── GameState.java         # DTO de estado
│   │       ├── GameController.java    # Controlador principal
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

### Configurar Categorías

Edita `src/resources/categories.properties`:

```properties
# Lista de categorías separadas por comas
categories=ANIMALES,PAISES,DEPORTES,OBJETOS,COMIDAS

# Nombres de visualización (opcional)
category.ANIMALES.display=Animales
category.PAISES.display=Países
```

**Nota:** Si agregas una nueva categoría, también debes agregarla al enum `Category.java` para mantener compatibilidad.

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
- **Arquitectura**: Separación de responsabilidades
- **Configuración**: JSON (palabras) + Properties (categorías y config)
- **UI**: Consola (funcional) + Swing (básica con diálogos)

## 📚 Conceptos Implementados

### Buenas Prácticas

- ✅ **Separación de responsabilidades** (SRP)
- ✅ **Inversión de dependencias** (DIP)
- ✅ **Open/Closed Principle** (extensible sin modificar)
- ✅ **Configuration externalization** (sin valores hardcodeados)
- ✅ **Testeable** (componentes desacoplados)
- ✅ **Singleton Pattern** (CategoryManager)

### Patrones de Diseño

- **MVC**: Model-View-Controller
- **Strategy**: Intercambio de vistas (GameView)
- **DTO**: GameState y WordsData para transferencia de datos
- **Factory**: WordLoader para crear WordEntry
- **Singleton**: CategoryManager para gestión centralizada

## ✨ Mejoras Recientes

- ✅ **Gson Integration**: Reemplazado parsing manual de JSON por Gson
- ✅ **CategoryManager**: Configuración centralizada de categorías
- ✅ **Validación mejorada**: Las categorías se validan contra configuración
- ✅ **Código más robusto**: Manejo de errores mejorado

## 🚧 Próximos Pasos

- [ ] Interfaz gráfica completa con Swing (actualmente usa diálogos básicos)
- [ ] Sistema de puntuación
- [ ] Estadísticas de partidas
- [ ] Modo multijugador
- [ ] Persistencia de configuración
- [ ] Tests unitarios
- [ ] Limpieza de código no utilizado

## 👥 Contribuir

Este es un proyecto educativo. Siéntete libre de:
- Agregar más palabras al JSON
- Mejorar la interfaz de consola
- Implementar la UI de Swing completa
- Agregar tests unitarios
- Mejorar la documentación

## 📝 Licencia

Proyecto educativo - Uso libre para fines educativos.

## 🐛 Problemas Conocidos

- La implementación Swing actual es básica (usa diálogos JOptionPane)
- No hay persistencia de datos entre sesiones
- No hay sistema de puntuación
- Algunos archivos no utilizados (pendiente de limpieza)

## 📖 Documentación Adicional

- [ARQUITECTURA.md](ARQUITECTURA.md) - Detalles de la arquitectura MVC

---

**Desarrollado con ❤️ para aprender POO y buenas prácticas de programación**
