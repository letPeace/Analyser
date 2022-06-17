package analysers;

import languages.Lang;
import languages.Language;
import parsers.Reader;

import java.util.*;
import java.util.Map.Entry;

public class Analyser{

    private ArrayList<SymbolData> text; // edited version
    // text contains symbols
    // a symbol may be a character, number, word or punctuation mark
    private LinkedHashMap<SymbolData, Integer> frequencies; // in lower case
    private LinkedHashMap<SymbolData, Integer> removedStopSymbols; // in lower case
    private HashMap<Language, Boolean> languages;

    public Analyser(){
        this.text = new ArrayList<>();
        this.frequencies = new LinkedHashMap<>();
        this.languages = new HashMap<>();
        this.removedStopSymbols = new LinkedHashMap<>();
    }

    // GET

    public ArrayList<SymbolData> getText(){
        return text;
    }

    public LinkedHashMap<SymbolData, Integer> getFrequencies(){
        return frequencies;
    }

    public HashMap<Language, Boolean> getLanguages() {
        return languages;
    }

    public Lang getLang(String symbol){
        char firstChar = symbol.charAt(0);
        if(firstChar >= 1040 && firstChar <= 1103) return Lang.RUSSIAN;
        if(firstChar >= 65 && firstChar <= 90
                || firstChar >= 97 && firstChar <= 122) return Lang.ENGLISH;
        if(firstChar >= 48 && firstChar <= 57) return Lang.NUMBER;
        if(firstChar >= 33 && firstChar <= 47
                || firstChar >= 58 && firstChar <= 64
                || firstChar >= 91 && firstChar <= 96
                || firstChar >= 123 && firstChar <= 126) return Lang.PUNCTUATION_MARK;
        return Lang.UNKNOWN;
    }

    // LANGUAGES

    public void addLanguage(Language language){
        languages.put(language, true);
    }

    public void makeLanguageDisable(Language language){
        if(!languageExists(language)) throw new RuntimeException("There is no such language as "+language.getCode());
        languages.put(language, false);
    }

    public void makeLanguageAble(Language language){
        if(!languageExists(language)) throw new RuntimeException("There is no such language as "+language.getCode());
        languages.put(language, true);
    }

    public boolean languageExists(Language language){
        return languages.containsKey(language);
    }

    // GENERATE

    public void generateFrequencies(Reader reader){
        generateText(reader);
        analyse();
    }

    public void generateText(Reader reader) throws NullPointerException{
        if(reader == null) throw new NullPointerException("Reader is undefined.");
        // copy text from Reader where it is stored
        for(String symbol : reader.getText()){
            text.add(new SymbolData(symbol, getLang(symbol)));
        }
    }

    // PRINT

    public void printLanguages(){
        for(Language language : languages.keySet()){
            System.out.println("LANGUAGE - "+language.getCode());
            for(Entry entry : language.getStopSymbols().entrySet()) System.out.println(entry.getKey()+" -> "+entry.getValue());
        }
    }

    public void printTextBySymbols(){
        // print text by separated symbols
        for(SymbolData symbolData : text){
            System.out.println("Symbol: \""+symbolData.getSymbol()+"\", lang: "+symbolData.getLang().getCode());
        }
    }

    public void printText(){
        // print whole text as it is stored in original file
        for(SymbolData symbolData : text) System.out.print(symbolData.getSymbol());
    }

    public void printFrequencies(){
        printMapWithSymbolsData(frequencies);
    }

    public void printRemovedStopSymbols(){
        printMapWithSymbolsData(removedStopSymbols);
    }

    private void printMapWithSymbolsData(LinkedHashMap<SymbolData, Integer> map){
        map.forEach((key, value) -> System.out.println(key + " -> " + value));
    }

    // SORT FREQUENCIES

    public void sortFrequenciesByKey(){
        putSortedEntries(getSortedEntries(true));
    }

    public void sortFrequenciesByValue(){
        putSortedEntries(getSortedEntries(false));
    }

    private ArrayList<Entry<SymbolData, Integer>> getSortedEntries(boolean sortByKey){
        // there are different algorithms depending on sorting by key or by value
        if(!sortByKey) sortFrequenciesByKey();
        // create ArrayList of frequencies entries
        ArrayList<Entry<SymbolData, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        // sort entries by key or by value
        Collections.sort(entries, (Entry<SymbolData, Integer> a, Entry<SymbolData, Integer> b)->{
            if(sortByKey) return a.getKey().getSymbol().compareTo(b.getKey().getSymbol());
            else return b.getValue().compareTo(a.getValue());
        });
        // return sorted entries
        return entries;
    }

    private void putSortedEntries(ArrayList<Entry<SymbolData, Integer>> entries){
        // delete all entries
        frequencies.clear();
        // fill frequencies with sorted entries
        for (Entry<SymbolData, Integer> entry : entries){
            frequencies.put(entry.getKey(), entry.getValue());
        }
    }

    // ANALYSE TEXT

    public void analyse(){ // this method calculates frequency of each symbol
        // delete previous data about frequencies
        HashSet<String> stopSymbols = getAllActiveStopSymbols();
        frequencies.clear();
        for(SymbolData symbolData : text){
            // convert each stop symbol to lower case
            String symbol = symbolData.getSymbolInLowerCase();
            // create new symbol data with edited symbol and the same lang
            SymbolData symbolDataInLowerCase = symbolData.getInLowerCase();
            // check if this symbol is stop or inviolable symbol
            boolean inviolableSymbol = symbol.charAt(0) == '\r' || symbol.charAt(0) == '\n' || symbol.charAt(0) == ' ';
            boolean stopSymbol = stopSymbols.contains(symbol);
            if(inviolableSymbol || stopSymbol) continue;
            // search for this symbol in frequencies
            incrementValue(frequencies, symbolDataInLowerCase);
        }
    }

    private void incrementValue(LinkedHashMap<SymbolData, Integer> map, SymbolData symbolData){
        Integer symbolFrequency;
        // if key "symbol" exists, increment its value
        try{
            symbolFrequency = map.get(symbolData);
            if(symbolFrequency == null) throw new NullPointerException("No such symbol in frequencies");
        } catch(NullPointerException e){
            // initialize start value
            symbolFrequency = 0;
        }
        // add new value of this symbol frequency
        map.put(symbolData, symbolFrequency+1);
    }

    // CLEAR TEXT

    public void clearTextFromSymbols(HashSet<String> stopSymbols){
        for(int i=text.size()-1; i>=0; i--){ // exactly from the end to the beginning
            // get another symbolData in the text
            SymbolData symbolDataInText = text.get(i);
            String symbol = symbolDataInText.getSymbolInLowerCase();
            // create new symbol data with edited symbol and the same lang
            SymbolData symbolDataRemoved = symbolDataInText.getInLowerCase();
            // check if this symbol equals a banned one
            for(String stopSymbol : stopSymbols){
                if(stopSymbol.equals(symbol)){
                    // if it does, add it in map of removed symbols
                    incrementValue(removedStopSymbols, symbolDataRemoved);
                    // delete it from the text
                    text.remove(i);
                    break;
                }
            }
        }
    }

    public void clearTextFromPunctuationMarksAndNumbers(){
        clearTextFromPunctuationMarks();
        clearTextFromNumbers();
    }

    public void clearTextFromPunctuationMarks(){
        clearTextFromLanguage(Lang.PUNCTUATION_MARK);
    }

    public void clearTextFromNumbers(){
        clearTextFromLanguage(Lang.NUMBER);
    }

    public void clearTextFromStopSymbols(Language language){
        // remove each active stop symbol from exactly this language
        clearTextFromSymbols(getActiveStopSymbols(language));
    }

    public void clearTextFromStopSymbolsOfLanguages(){
        // remove each active stop symbol from each language from the text
        clearTextFromSymbols(getAllActiveStopSymbols());
    }

    public void clearTextFromLanguage(Lang lang){
        for(int i=text.size()-1; i>=0; i--){ // exactly from the end to the beginning
            // get another symbolData in the text
            SymbolData symbolDataInText = text.get(i);
            // get symbol's lang
            Lang symbolLang = symbolDataInText.getLang();
            // if it equals given lang, remove it from the text
            if(symbolLang == lang) text.remove(i);
        }
    }

    // STOP SYMBOLS

    private HashSet<String> getAllActiveStopSymbols(){
        // from each added language
        HashSet<String> activeStopSymbols = new HashSet<>();
        // this set will contain each stop symbol in each language
        for(Language language : languages.keySet()){
            // check each existing language
            // if language is unable, do not add its stop symbols
            if(!languages.get(language)) continue;
            activeStopSymbols.addAll(getActiveStopSymbols(language));
        }
        return activeStopSymbols;
    }

    private HashSet<String> getActiveStopSymbols(Language language){
        HashSet<String> activeStopSymbols = new HashSet<>();
        // each stop symbol is already in lower case
        var stopSymbols = language.getStopSymbols();
        for(String stopSymbol : stopSymbols.keySet()){
            // check each stop symbol in the language
            if(stopSymbols.get(stopSymbol)){
                // if this stop symbol is ABLE, add it
                activeStopSymbols.add(stopSymbol);
            }
        }
        return activeStopSymbols;
    }

    // CASE

    public void convertToCase(boolean toUpperCase){
        for(SymbolData symbolData : text){
            String symbol = symbolData.getSymbol();
            // change each symbol's case to upper or lower
            symbolData.setSymbol(toUpperCase ? symbol.toUpperCase() : symbol.toLowerCase());
        }
    }

}
