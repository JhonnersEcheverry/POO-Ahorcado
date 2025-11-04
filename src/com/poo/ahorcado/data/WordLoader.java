package com.poo.ahorcado.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poo.ahorcado.config.CategoryManager;
import com.poo.ahorcado.model.Category;
import com.poo.ahorcado.model.WordEntry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga palabras desde un archivo JSON de recursos usando Gson.
 * 
 * Formato esperado:
 * {
 *   "words": [
 *     {"word": "PALABRA", "category": "CATEGORIA", "hint": "Pista"}
 *   ]
 * }
 * 
 * Esta implementación usa Gson para parsear JSON de forma robusta
 * y mantenible.
 */
public class WordLoader {
    
    private static final String WORDS_FILE = "/resources/words.json";
    private final Gson gson;
    private final CategoryManager categoryManager;
    
    /**
     * Constructor por defecto que inicializa Gson y CategoryManager.
     */
    public WordLoader() {
        this.gson = new GsonBuilder().create();
        this.categoryManager = CategoryManager.getInstance();
    }
    
    /**
     * Constructor alternativo que permite inyectar un Gson (útil para testing).
     */
    public WordLoader(Gson gson) {
        this.gson = gson;
        this.categoryManager = CategoryManager.getInstance();
    }
    
    /**
     * Carga todas las palabras desde el archivo JSON de recursos.
     * 
     * @return Lista de WordEntry cargadas desde el archivo
     * @throws RuntimeException si no se puede cargar o parsear el archivo
     */
    public List<WordEntry> loadWords() {
        try (InputStream inputStream = getClass().getResourceAsStream(WORDS_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("No se encontró el archivo de palabras: " + WORDS_FILE);
            }
            
            // Deserializar JSON usando Gson
            WordsData wordsData = gson.fromJson(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8), 
                WordsData.class
            );
            
            // Validar que se cargaron palabras
            if (wordsData == null || wordsData.getWords() == null || wordsData.getWords().isEmpty()) {
                throw new RuntimeException("No se cargaron palabras. El archivo está vacío o no tiene formato válido.");
            }
            
            // Convertir WordData a WordEntry
            return convertToWordEntries(wordsData.getWords());
            
        } catch (IOException e) {
            throw new RuntimeException("Error al leer o parsear el archivo de palabras: " + WORDS_FILE, e);
        }
    }
    
    /**
     * Convierte una lista de WordData (del JSON) a WordEntry (modelo del dominio).
     */
    private List<WordEntry> convertToWordEntries(List<WordsData.WordData> wordDataList) {
        List<WordEntry> entries = new ArrayList<>();
        
        for (WordsData.WordData wordData : wordDataList) {
            try {
                // Validar campos requeridos
                if (wordData.getWord() == null || wordData.getWord().trim().isEmpty()) {
                    System.err.println("Advertencia: palabra vacía encontrada, omitiendo entrada.");
                    continue;
                }
                
                if (wordData.getHint() == null || wordData.getHint().trim().isEmpty()) {
                    System.err.println("Advertencia: pista vacía para palabra '" + wordData.getWord() + "', omitiendo entrada.");
                    continue;
                }
                
                if (wordData.getCategory() == null || wordData.getCategory().trim().isEmpty()) {
                    System.err.println("Advertencia: categoría vacía para palabra '" + wordData.getWord() + "', omitiendo entrada.");
                    continue;
                }
                
                // Validar y convertir categoría usando CategoryManager
                String categoryStr = wordData.getCategory().toUpperCase().trim();
                
                // Validar contra configuración centralizada
                if (!categoryManager.isValidCategory(categoryStr)) {
                    System.err.println("Error: categoría inválida '" + wordData.getCategory() + 
                                     "' para palabra '" + wordData.getWord() + 
                                     "'. Categorías válidas: " + String.join(", ", categoryManager.getCategories()) + 
                                     ". Omitiendo entrada.");
                    continue;
                }
                
                // Convertir a Category desde configuración
                Category category;
                try {
                    category = Category.fromString(categoryStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage() + ". Omitiendo entrada.");
                    continue;
                }
                
                // Crear WordEntry
                entries.add(new WordEntry(
                    wordData.getWord().trim().toUpperCase(),
                    category,
                    wordData.getHint().trim()
                ));
                
            } catch (Exception e) {
                System.err.println("Error al procesar entrada de palabra: " + e.getMessage());
                // Continuar con la siguiente entrada en lugar de fallar completamente
            }
        }
        
        if (entries.isEmpty()) {
            throw new RuntimeException("No se pudo cargar ninguna palabra válida desde el archivo.");
        }
        
        return entries;
    }
    
}


