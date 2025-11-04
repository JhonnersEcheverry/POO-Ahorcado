package com.poo.ahorcado.model;

import com.poo.ahorcado.config.CategoryManager;

import java.util.Objects;

/**
 * Representa una categoría de palabras.
 * Las categorías se cargan dinámicamente desde configuración,
 * permitiendo agregar nuevas sin recompilar el código.
 */
public class Category {
    
    private final String name;
    
    /**
     * Constructor privado. Las categorías se crean desde CategoryManager.
     */
    private Category(String name) {
        this.name = name.toUpperCase();
    }
    
    /**
     * Obtiene el nombre de la categoría.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Crea o obtiene una categoría desde su nombre (string).
     * Valida que la categoría exista en la configuración.
     * 
     * @param name Nombre de la categoría
     * @return Category correspondiente
     * @throws IllegalArgumentException si la categoría no está configurada
     */
    public static Category fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        
        String categoryName = name.toUpperCase().trim();
        CategoryManager manager = CategoryManager.getInstance();
        
        if (!manager.isValidCategory(categoryName)) {
            throw new IllegalArgumentException(
                "Categoría inválida: " + categoryName + 
                ". Categorías válidas: " + String.join(", ", manager.getCategories())
            );
        }
        
        return new Category(categoryName);
    }
    
    /**
     * Obtiene todas las categorías disponibles desde la configuración.
     */
    public static Category[] values() {
        CategoryManager manager = CategoryManager.getInstance();
        return manager.getCategories().stream()
                .map(Category::new)
                .toArray(Category[]::new);
    }
    
    /**
     * Obtiene el nombre de visualización de la categoría.
     */
    public String getDisplayName() {
        CategoryManager manager = CategoryManager.getInstance();
        return manager.getDisplayName(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
