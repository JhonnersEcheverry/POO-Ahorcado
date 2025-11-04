
package com.poo.ahorcado.logic;

import com.poo.ahorcado.data.WordLoader;
import com.poo.ahorcado.model.Category;
import com.poo.ahorcado.model.WordEntry;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Banco de palabras. Carga palabras desde un archivo externo
 * y proporciona acceso aleatorio a ellas.
 */
public class WordBank {
    private final List<WordEntry> entries;
    private final Random random;
    
    /**
     * Constructor que carga palabras desde el archivo de recursos.
     */
    public WordBank() {
        this.random = new Random(System.nanoTime());
        WordLoader loader = new WordLoader();
        this.entries = loader.loadWords();
    }
    
    /**
     * Constructor alternativo que permite inyectar palabras (útil para testing).
     */
    public WordBank(List<WordEntry> entries) {
        this.random = new Random(System.nanoTime());
        this.entries = entries;
    }

    public WordEntry randomAny() {
        return entries.get(random.nextInt(entries.size()));
    }

    public WordEntry randomByCategory(Category c) {
        List<WordEntry> filtered = entries.stream()
                .filter(e -> e.getCategory().equals(c))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return randomAny(); // fallback if category is empty
        }
        return filtered.get(random.nextInt(filtered.size()));
    }
    
}
