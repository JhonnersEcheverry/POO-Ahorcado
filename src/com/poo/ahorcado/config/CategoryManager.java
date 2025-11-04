package com.poo.ahorcado.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Gestiona las categorías cargadas desde un archivo de configuración.
 * Permite centralizar la configuración de categorías sin modificar código.
 */
public class CategoryManager {
    
    private static final String CATEGORIES_FILE = "/resources/categories.properties";
    private static CategoryManager instance;
    private final List<String> categories;
    private final Properties properties;
    
    private CategoryManager() {
        this.properties = new Properties();
        this.categories = new ArrayList<>();
        loadCategories();
    }
    
    /**
     * Obtiene la instancia singleton del CategoryManager.
     */
    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }
    
    /**
     * Carga las categorías desde el archivo de configuración.
     */
    private void loadCategories() {
        try (InputStream inputStream = getClass().getResourceAsStream(CATEGORIES_FILE)) {
            if (inputStream == null) {
                // Si no existe el archivo, usar valores por defecto
                System.out.println("Advertencia: No se encontró " + CATEGORIES_FILE + 
                                 ". Usando categorías por defecto.");
                loadDefaultCategories();
                return;
            }
            
            properties.load(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
            
            String categoriesStr = properties.getProperty("categories", "");
            if (categoriesStr == null || categoriesStr.trim().isEmpty()) {
                loadDefaultCategories();
                return;
            }
            
            // Parsear categorías separadas por comas
            String[] catArray = categoriesStr.split(",");
            for (String cat : catArray) {
                String trimmed = cat.trim().toUpperCase();
                if (!trimmed.isEmpty()) {
                    categories.add(trimmed);
                }
            }
            
            if (categories.isEmpty()) {
                loadDefaultCategories();
            }
            
        } catch (IOException e) {
            System.err.println("Error al cargar categorías desde " + CATEGORIES_FILE + 
                             ": " + e.getMessage());
            loadDefaultCategories();
        }
    }
    
    /**
     * Carga las categorías por defecto si no se puede cargar desde configuración.
     */
    private void loadDefaultCategories() {
        categories.clear();
        categories.add("ANIMALES");
        categories.add("PAISES");
        categories.add("DEPORTES");
        categories.add("OBJETOS");
        categories.add("COMIDAS");
    }
    
    /**
     * Obtiene todas las categorías configuradas.
     */
    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }
    
    /**
     * Verifica si una categoría es válida.
     */
    public boolean isValidCategory(String category) {
        if (category == null) return false;
        return categories.contains(category.toUpperCase().trim());
    }
    
    /**
     * Obtiene el nombre de visualización de una categoría.
     */
    public String getDisplayName(String category) {
        if (category == null) return null;
        String key = "category." + category.toUpperCase() + ".display";
        String displayName = properties.getProperty(key);
        return displayName != null ? displayName : category;
    }
}

