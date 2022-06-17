package languages;

import parsers.Reader;

import java.util.ArrayList;
import java.util.HashMap;

public class Language{
    // Language may be RU, EN, punctuation marks

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

}
