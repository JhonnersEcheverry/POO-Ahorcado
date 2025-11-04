package com.poo.ahorcado.data;

import java.util.List;

/**
 * Clase DTO para mapear la estructura JSON del archivo de palabras.
 * Usada por Gson para deserializar el JSON.
 * 
 * Gson mapea automáticamente por nombre de campo, así que no necesitamos
 * anotaciones especiales. Los nombres de los campos deben coincidir exactamente
 * con los nombres en el JSON.
 */
public class WordsData {
    
    private List<WordData> words;
    
    public List<WordData> getWords() {
        return words;
    }
    
    public void setWords(List<WordData> words) {
        this.words = words;
    }
    
    /**
     * Clase interna para representar cada entrada de palabra en el JSON.
     */
    public static class WordData {
        
        private String word;
        private String category;
        private String hint;
        
        public String getWord() {
            return word;
        }
        
        public void setWord(String word) {
            this.word = word;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getHint() {
            return hint;
        }
        
        public void setHint(String hint) {
            this.hint = hint;
        }
    }
}

