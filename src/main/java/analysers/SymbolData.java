package analysers;

import languages.Lang;

public class SymbolData{

    private String symbol;
    private Lang lang;

    public SymbolData(String symbol, Lang lang) {
        this.symbol = symbol;
        this.lang = lang;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public SymbolData getInLowerCase(){
        return new SymbolData(getSymbolInLowerCase(), getLang());
    }

    public String getSymbolInLowerCase(){
        return new String(this.getSymbol().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolData symbolData = (SymbolData) o;
        return getSymbol().equals(symbolData.getSymbol());
    }

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }

    @Override
    public String toString() {
        return "SymbolData{" +
                "symbol='" + symbol + '\'' +
                ", lang=" + lang.getCode() +
                '}';
    }
}

