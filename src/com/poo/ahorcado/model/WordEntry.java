package com.poo.ahorcado.model;


public class WordEntry {
    private final String word;     
    private final Category category;
    private final String textHint;  
    
    public WordEntry(String word, Category category, String textHint) {
        this.word = word.toUpperCase();
        this.category = category;
        this.textHint = textHint;  
    }
    
    public String getWord() { return word; }
    
    public Category getCategory() { return category; }
    
    public String getTextHint() { return textHint; }
}
