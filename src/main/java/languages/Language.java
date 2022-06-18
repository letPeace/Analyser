package languages;

import parsers.Reader;

import java.util.HashMap;

public class Language{
    // Language may be RU, EN, punctuation marks (PM)

    private String code;
    private HashMap<String, Boolean> stopSymbols; // all stop symbols are in lower case

    public Language(String code){
        this.code = code;
        this.stopSymbols = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public HashMap<String, Boolean> getStopSymbols() {
        return stopSymbols;
    }

    public void generateStopSymbols(Reader reader){
        // copy each symbol from Reader where they are stored
        for(String stopSymbol : reader.getText()){
            // check if symbol is \r, \n or space
            char stopSymbolChar = stopSymbol.charAt(0);
            boolean inviolableSymbol = stopSymbolChar == '\r' || stopSymbolChar == '\n' || stopSymbolChar == ' ';
            // if it is, do not add them to HashMap
            if(!inviolableSymbol) addStopSymbol(stopSymbol);
        }
    }

    public void makeDisable(String symbol){
        if(!keyExists(symbol)) throw new RuntimeException("There is no such symbol as "+symbol);
        getStopSymbols().put(symbol.toLowerCase(), false);
    }

    public void makeAble(String symbol){
        if(!keyExists(symbol)) throw new RuntimeException("There is no such symbol as "+symbol);
        getStopSymbols().put(symbol.toLowerCase(), true);
    }

    public boolean keyExists(String symbol){
        return getStopSymbols().containsKey(symbol.toLowerCase());
    }

    public void addStopSymbol(String stopSymbol){
        getStopSymbols().put(stopSymbol.toLowerCase(), true);
    }

    public void printStopSymbols(){
        stopSymbols.forEach((key, value) -> System.out.println("Symbol: \"" + key + "\" -> " + value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return getCode().equals(language.getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }

}

