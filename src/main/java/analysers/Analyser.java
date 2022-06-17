package analysers;

import languages.Language;
import parsers.Reader;

import java.util.*;
import java.util.Map.Entry;

public class Analyser{

    private ArrayList<String> text; // edited version
    // text contains symbols
    // a symbol may be a character, number, word or punctuation mark
    private LinkedHashMap<String, Integer> frequencies;
    private HashSet<Language> languages;
    private LinkedHashSet<String> removedStopSymbols;

    public Analyser(){
        this.text = new ArrayList<>();
        this.frequencies = new LinkedHashMap<>();
        this.languages = new HashSet<>();
        this.removedStopSymbols = new LinkedHashSet<>();
    }

    public ArrayList<String> getText() {
        return text;
    }

    public LinkedHashMap<String, Integer> getFrequencies() {
        return frequencies;
    }

    // PRINT

    public void printTextBySymbols(){
        // print text by separated symbols
        for(String symbol : text){
            System.out.println("S~"+symbol+"~E");
        }
    }

    public void printText(){
        // print whole text as it is stored in original file
        text.forEach(System.out::print);
    }

    public void printFrequencies(){
        frequencies.forEach((key, value) -> System.out.println("Symbol: \"" + key + "\" -> " + value));
    }

    public void printRemovedStopSymbols(){
        removedStopSymbols.forEach(System.out::print);
    }

}
