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

    public void addLanguage(Language language){
        languages.add(language);
    }

    public void generateFrequencies(Reader reader){
        generateText(reader);
        analyse();
    }

    public void generateText(Reader reader){
        // copy text from Reader where it is stored
        text.addAll(reader.getText());
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

    // SORT FREQUENCIES

    public void sortFrequenciesByKey(){
        putSortedEntries(getSortedEntries(true));
    }

    public void sortFrequenciesByValue(){
        putSortedEntries(getSortedEntries(false));
    }

    private ArrayList<Entry<String, Integer>> getSortedEntries(boolean sortByKey){
        // there are different algorithms depending on sorting by key or by value
        if(!sortByKey) sortFrequenciesByKey();
        // create ArrayList of frequencies entries
        ArrayList<Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        // sort entries by key or by value
        Collections.sort(entries, (Entry<String, Integer> a, Entry<String, Integer> b) ->{
            if(sortByKey) return a.getKey().compareTo(b.getKey());
            else return b.getValue().compareTo(a.getValue());
        });
        // return sorted entries
        return entries;
    }

    private void putSortedEntries(ArrayList<Entry<String, Integer>> entries){
        // delete all entries
        frequencies.clear();
        // fill frequencies with sorted entries
        for (Entry<String, Integer> entry : entries){
            frequencies.put(entry.getKey(), entry.getValue());
        }
    }

    // ANALYSE TEXT

    public void analyse(){ // this method calculates frequency of each symbol
        // delete previous data about frequencies
        frequencies.clear();
        for(String symbol : text){
            // convert each stop symbol to lower case
            symbol = symbol.toLowerCase();
            // check if this symbol is stop or inviolable symbol
            boolean inviolableSymbol = symbol.charAt(0) == '\r' || symbol.charAt(0) == '\n' || symbol.charAt(0) == ' ';
            boolean stopSymbol = getStopSymbols().contains(symbol);
            if(inviolableSymbol || stopSymbol) continue;
            // search for this symbol in HashMap with frequencies
            Integer symbolFrequency;
            try{
                // if key "symbol" exists, increment its value
                symbolFrequency = frequencies.get(symbol);
                if(symbolFrequency == null) throw new NullPointerException("No such symbol in HashMap frequencies");
            } catch(NullPointerException e){
                // initialize start value
                symbolFrequency = 0;
            }
            // add new value of this symbol frequency
            frequencies.put(symbol, symbolFrequency+1);
        }
    }

    // CLEAR TEXT

    public void clearText(HashSet<String> stopSymbols){
        for(int i=text.size()-1; i>=0; i--){
            // get another symbol in the text
            String symbolInText = text.get(i).toLowerCase();
            // check if this symbol equals a banned one
            for(String symbol : stopSymbols){
                if(symbolInText.equals(symbol)){
                    // if it does, delete it from the text
                    removedStopSymbols.add(symbol);
                    text.remove(i);
                    break;
                }
            }
        }
    }

    public void clearText(){ // remove each stop word from the text
        clearText(getStopSymbols());
    }

    // STOP SYMBOLS

    private HashSet<String> getStopSymbols(){
        HashSet<String> stopSymbolsFromEachLanguage = new HashSet<>();
        // this set will contain each stop symbol in each language
        for(Language language : languages){
            // check each existing language
            // each stop symbol is already in lower case
            var stopSymbols = language.getStopSymbols();
            for(String stopSymbol : stopSymbols.keySet()){
                // check each stop symbol in the language
                if(stopSymbols.get(stopSymbol)){
                    // if this stop symbol is ABLE, add it
                    stopSymbolsFromEachLanguage.add(stopSymbol);
                }
            }
        }
        return stopSymbolsFromEachLanguage;
    }

    // CASE

    public void convertToCase(boolean toUpperCase){
        for(int i=0; i<text.size(); i++){
            String symbol = text.get(i);
            // change each symbol's case to upper or lower
            text.set(i, toUpperCase ? symbol.toUpperCase() : symbol.toLowerCase());
        }
    }

}
